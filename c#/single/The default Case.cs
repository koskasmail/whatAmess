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
            int num = Convert.ToInt32(Console.ReadLine());

            /*
            1 - Easy
            2 - Medium
            3 - Hard
            other - "Invalid option"
            */

            //your code goes here
                       switch(num) {
                case 1:
                    System.Console.WriteLine("Easy");
                    break;
                case 2:
                    System.Console.WriteLine("Medium");
                    break;
                case 3:
                    System.Console.WriteLine("Hard");
                    break;
                default:
                    System.Console.WriteLine("other");
                    break;
            }
        }
    }
}
