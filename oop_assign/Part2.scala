
// Define an abstract generic class Tree, parameterized by a type T, such that:
// (1) Instances of Tree are covariantly subtyped, i.e. if B is a subtype of A, then
//     Tree[B] is a subtype of Tree[A].
// (2) The method, map, is defined for all trees, where map takes a function f as a parameter
//     and returns a tree resulting from applying f to each value associated with a 
//     Leaf or Node in the tree. This is essentially the same as the mapTree you wrote in ML.
//     Hint: No code for map is needed here (other than the declaration of its type).

// This definition should be roughly 3 lines, including 
                              // closing "}"
abstract class Tree[+T]
{
    def map[E](f:T=>E):Tree[E]
}

  
// Define a generic case class Leaf, parameterized by a type T, such that:
// (1) Leaf[T] is a subtype of Tree[T]
// (2) Leaf[T] takes a parameter x of type T. That is, a Leaf[T] has a value of type T 
//     associated with it.
// (3) The toString() method in Leaf[T] is overriden to print something sensible,
//     that includes the value of x.
// (4) The map method (see class Tree[], above) is overridden.
// Roughly 4 lines

case class Leaf[T](x:T) extends Tree[T]
{
    override def toString() = "Leaf("+x+")"
    override def map[E](f:T=>E):Tree[E] = new Leaf[E](f(x))
    // override def map[E](f:Tree[T]=>E):List[E] = List(f(this))
}

// Define a generic case class Node, parameterized by a type T, such that:
// (1) Node[T] is a subtype of Tree[T]
// (2) Node[T] takes a parameter x of type T and a parameter children that is
//     a list of Tree[T]'s.  That is, like in the ML assignment (but with each 
//     internal node having a label as well).
// (3) The toString() method in Node[T] is overriden to print something sensible,
//     that includes the value of x.
// (4) The map method (see class Tree[], above) is overridden.
//     Hint:  In defining the map method for Node[T], you can use the 
//     built-in map method for the List[] class. 
//     (see http://www.scala-lang.org/api/2.11.8/#scala.collection.immutable.List)
// Roughly 4 lines

case class Node[T](x:T, children:List[Tree[T]]) extends Tree[T]
{
    override def toString() = 
    {
        var res:String = "Node("+ x + ",List("
        if(children.isEmpty)
        {
            res += ")"
        }
        else
        {
            for(child<-children)
                res += child + ","
            res = res.substring(0, res.length - 1) // eliminate last ,
            res += ")"
        }
        res + ")" // return value
    }
    override def map[E](f:T=>E):Tree[E] =
    {
        new Node[E](f(x),children.map((t)=>t.map(f)))
    }
    // override def map[E](f:Tree[T]=>E):List[E] = 
    // {
    //     List(f(new Leaf(x))) ++ children.map[E](f)
    // }
    // override def map[E](f:T=>E):Tree[E] =
    // {
    //     new Node[E](f(x),children.map[E](f))
    // }
}


// Define a simple class A such that:
// (1) It takes a parameter x of type Int
// (2) It defines a method, value, that returns the value of x
// (3) It define a + method (i.e. the name of the method is "+") 
//     that takes another A object, other, as a parameter and returns a
//     new A object created with the result of adding this.value and
//     other.value together.
// (4) it overrides the toString method to print something sensible, 
//     showing x (see the sample output below).
// Roughly 5 lines
class A(k:Int)
{
    def value():Int = k
    def +(other:A):A = new A(this.value() + other.value())
    override def toString() = "A("+ k + ")"
}  

// Define a simple class B such that:
// (1) B is a subtype of A.
// (2) B takes two parameters, x and y, both of type Int.
// (2) It overrides the value method to return the value of x+y
// (4) it overrides the toString method to print something sensible,
//     showing x and y (see the sample output below).
// Roughly 4 lines
class B(x:Int, y:Int) extends A(x+y)
{
    override def value():Int = x + y
    override def toString() = "B("+ x +","+ y + ")"
}


// Define a singleton class named Part2 that defines a several functions, 
// As described below.
    
object Part2 {

//  Define that method breadthFirst that, for any type T, takes a
//  Tree[T] as a parameter and returns a List[T] containing the values
//  at the leaves and nodes when the tree is traversed in BREADTH
//  FIRST order. You can use any (reasonable) algorithm for this that
//  you like. Here's code I wrote in ML:
//
//  fun breadthFirst tr =
//   let
//    fun breadth [] values = values
//     |  breadth ((leaf x)::rest) values = breadth rest (values @ [x])
//     |  breadth ((node (x, left, right))::rest) values =
//             breadth (rest @ [left, right]) (values @ [x])
//   in
//      breadth [tr] []
//   end
// Roughly 9 lines
    def breadth[T](work_list:List[Tree[T]], res_list:List[T]):List[T] =
    {
        if(work_list.isEmpty)
        {
            res_list
        }
        else
        {
            work_list.head match
            {
                case Leaf(x) => 
                {
                    breadth[T](work_list.slice(1, work_list.length), res_list ++ List(x))
                }
                case Node(x, children) => 
                {
                    breadth[T](work_list.slice(1, work_list.length) ++ children, res_list ++ List(x))
                }
            }
        }
    }

    def breadthFirst[T](tree:Tree[T]):List[T] =
    {
        breadth[T](List(tree), List[T]())
    }


// Define the function reduce, which like the reduce function you wrote for the 
// ML assignment, takes a function f, a value b, and a list L and:
//  - if L is empty, returns b.
//  - otherwise, assuming L is a list of the form List(x1, x2, ..., xn), 
//    returns f(x1,f(x2,...f(xn,b)...)))).  
// Be sure to make reduce as polymorphic as possible.
// Roughly 4 lines
    def reduce[T,E](f:(T,E)=>E, b:E, L:List[T]):E =
    {
        if(L.isEmpty)
        {
            b
        }
        else
        {
            f(L.head, reduce[T,E](f, b, L.slice(1, L.length)))
        }
    }


// Define the function sumTreeA, which takes a parameter of type Tree[A] 
// and returns the sum of all the A values found at the leaves and nodes in the tree.
// The return type (the sum) should be an object of type A.  Note that, in order to 
// call sumTreeA on a Tree[B], covariant subtyping on instances of Tree[] is required,
// as specified above.
// You should use the map method of the List class and use the above reduce
// function.
// Roughly 4 lines
    

    def sumTreeA(t: Tree[A]):A =
    {
        t match
        {
            case Leaf(x) => x
            case Node(x, children) =>
            {
                reduce[A,A]((x,y)=>x+y, x, children.map(sumTreeA))
            }
            // case Node(x, children) =>
            // {
            //     reduce[A,A]((x,y)=>x+y, new A(0), t.map[A](sumTreeA))
            // }
        }
        
    }

// Leave this main procedure as is.  The output when you run the program should be similar
// to what is shown below.

  def main(args: Array[String]): Unit = {
    val t1 = Node(new A(10), List(Node(new A(9), List(Leaf(new A(7)), 
                                  Leaf(new A(6)))), Node(new A(8), 
                                  List(Leaf(new A(5)), Leaf(new A(4))))))
    println("t1 = "+t1)
    println("breadthFirst(t1) =" + breadthFirst(t1))
    println("sumTreeA(t1) = " + sumTreeA(t1));
    val t2 = t1.map((a:A)=>new A(a.value*2))
    println("t2 = "+t2)
    println("breadthFirst(t2) =" + breadthFirst(t2))
    println("sumTreeA(t2) = " + sumTreeA(t2));
    val t3 = Node(new B(5,5), List(Node(new B(5,4), List(Leaf(new B(3,4)), 
                                   Leaf(new B(1,5)))), Node(new B(4,4), 
                                   List(Leaf(new B(3,2)), Leaf(new B(4,0))))))
    println("t3 = "+t3)
    println("breadthFirst(t3) =" + breadthFirst(t3))
    println("sumTreeA(t3) = " + sumTreeA(t3));
  }


// This is what the output should look like:

// t1 = Node(A(10),List(Node(A(9),List(Leaf(A(7)), Leaf(A(6)))), Node(A(8),List(Leaf(A(5)), Leaf(A(4))))))
// breadthFirst(t1) =List(A(10), A(9), A(8), A(7), A(6), A(5), A(4))
// sumTreeA(t1) = A(49)
// t2 = Node(A(20),List(Node(A(18),List(Leaf(A(14)), Leaf(A(12)))), Node(A(16),List(Leaf(A(10)), Leaf(A(8))))))
// breadthFirst(t2) =List(A(20), A(18), A(16), A(14), A(12), A(10), A(8))
// sumTreeA(t2) = A(98)
// t3 = Node(B(5,5),List(Node(B(5,4),List(Leaf(B(3,4)), Leaf(B(1,5)))), Node(B(4,4),List(Leaf(B(3,2)), Leaf(B(4,0))))))
// breadthFirst(t3) =List(B(5,5), B(5,4), B(4,4), B(3,4), B(1,5), B(3,2), B(4,0))
// sumTreeA(t3) = A(49)

}


