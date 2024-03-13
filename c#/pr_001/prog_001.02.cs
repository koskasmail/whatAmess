
using System; ///----[name space]----///

namespace jaron
{
    public class ProgramBase ///----[class]----///
    {
        public static void Main(string[] args)
        {
            ///----[Text]----///
            Console.WriteLine("Welcome to the Code Playground");

            ///----[number]----///
            Console.WriteLine(404);

            ///----[witout 'using System' namespace ]----///
            System.Console.WriteLine("text witout 'using System' namespace ");
            System.Console.WriteLine(156);

            int age = 77;
            double pi = 3.14;
            char character = 'c';
            string name = "jane doe";
            bool flag = true;

            System.Console.WriteLine("age = " + age);
            System.Console.WriteLine("name = " + name);
            System.Console.WriteLine("character = " + character);
            System.Console.WriteLine("flag = " + flag);

            Console.WriteLine("Name ==> " + name + ".....Age ==> " +  age);
            Console.WriteLine("Name ==> {0} ..... age ==> {1} ",name ,age);
            Console.WriteLine("pi ==> {0} ..... character ==> {1} ..... flag ==> {2} ", pi, character, flag);

            fnVariable();
        }

        static void fnVariable()
        {
            System.Console.WriteLine("test the function");
        }
    }
} 
