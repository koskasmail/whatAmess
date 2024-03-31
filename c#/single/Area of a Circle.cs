using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SoloLearn
{
    class Program
    {
        static void Main(string[] args)
        {
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
    }
}
