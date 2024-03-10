using System;
using System.IO;

class Program
{
    static void Main(string[] args)
    {
        try
        {
            string filePath = "example.txt"; // Change this to the path of your text file

            if (File.Exists(filePath))
            {
                string text = File.ReadAllText(filePath);
                Console.WriteLine(text);
            }
            else
            {
                Console.WriteLine($"Error: File '{filePath}' not found.");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }
    }
}
