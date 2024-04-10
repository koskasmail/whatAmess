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
            fnSwitchStatement();
            //fnIfStatement();
            //fnCircleArea();
            //fnCalc();
            //fnVar();
            //fnInputConvert();
            //fnInput();
            //fnOutput();
        }


        static void fnSwitchStatement() {

        }

        static void fnIfStatement() {

            int x = 8;
            int y = 3;

            if (x > y)
            {
                Console.WriteLine("x is greater than y");
            }

            int age = Convert.ToInt32(Console.ReadLine());
            if (age <= 19)
            {
                System.Console.WriteLine(age + "Take your kindle");
            }
        }

        static void fnCircleArea() {
            const double pi = 3.14;
            double radius;
            double circleArea;

            //your code goes here
            System.Console.WriteLine("Enter a radius number: ");
            radius = Convert.ToDouble(System.Console.ReadLine());
            System.Console.WriteLine("You have enter: {0}", radius);
            circleArea = (radius * radius) * pi;
            System.Console.WriteLine("fnCircleArea Area is {0}", circleArea);
        }

        static void fnCalc() {
            //your code goes here
            int toys = 15;
            int toysperpack = 3;
            int remainder = toys % toysperpack;
            System.Console.WriteLine(toys * toysperpack);
            System.Console.WriteLine(toys / toysperpack);
            System.Console.WriteLine(toys + toysperpack);
            System.Console.WriteLine(toys - toysperpack);
            System.Console.WriteLine(toys % toysperpack);
            System.Console.WriteLine("toys = ", toys);
            System.Console.WriteLine("++toys={0} --toys={1}, toys++={2}, toys--=={3}", ++toys, --toys, toys++, toys--);
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

            const double PI = 3.14159265358979;
            System.Console.WriteLine("PI ==> {0} ..... pi ==> {1} ", PI, pi);
        }
    }
} 
