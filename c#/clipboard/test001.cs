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
            //your code goes here
            int toys = 142;
            int toysperpack = 15;
            int remainder = toys % toysperpack;
            System.Console.writeLine(remainder);       
        }
    }
}
