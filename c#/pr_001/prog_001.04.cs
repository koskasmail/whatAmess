/*
 * Jaron - 2024.03.17
 */
using System; ///----[name space]----///

namespace jaron
{
    public class ProgramBase ///----[class]----///
    {
        public static void Main(string[] args)
        {
            fnVar();
            //fnInputConvert();
            //fnInput();
            ///fnOutput();
        }

        static void fnVar()
        {
            var num = 22;
            System.Console.WriteLine(num);

        /*  var variableError;
            variableError = 22;*/
        }

        static void fnInputConvert()
        {
            int age = 2;
            bool blbol;
            String stbol;

            System.Console.WriteLine("Enter you'r age: ");
            age = Convert.ToInt32(System.Console.ReadLine());

            ///System.Console.WriteLine("true / false: ");
            ///blbol = Convert.ToBoolean(System.Console.ReadLine());

            if (age >= 18) {
                blbol = true;
            }
            else
            {
                blbol = false;
            }

            stbol = Convert.ToString(blbol);
            
            System.Console.WriteLine("\n\n----------[O U T P U T]----------");
            System.Console.WriteLine("You'r Age is:  {0}", age);
            System.Console.WriteLine("boolean is:  {0}", blbol);
            System.Console.WriteLine("boolean to string:  {0}", stbol);
            System.Console.WriteLine("-------------------------------------");
        }

        static void fnInput()
        {
            String stName = "";
            System.Console.WriteLine("Enter you'r name: ");
            stName = System.Console.ReadLine();
            System.Console.WriteLine("Welcome {0}", stName);
        }

        static void fnOutput()
        {
            ///----[Text]----///
            System.Console.WriteLine("T e x t");

            ///----[number]----///
            System.Console.WriteLine(404);

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

            System.Console.WriteLine("Name ==> " + name + ".....Age ==> " + age);
            System.Console.WriteLine("Name ==> {0} ..... age ==> {1} ", name, age);
            System.Console.WriteLine("pi ==> {0} ..... character ==> {1} ..... flag ==> {2} ", pi, character, flag);

        }
    }
} 
