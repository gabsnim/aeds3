import Menus.*;
import java.util.Scanner;


public class Main
{
    public static void main(String[] args) 
    {

        Scanner scan;

        try 
        {
            scan = new Scanner(System.in);
            int opcao;
            do 
            {

                System.out.println("PUCFlix 1.0\n" +
                "-----------\n" +
                "> Inicio\n\n" +
                "1) Series\n" + 
                "2) Episodios\n" + 
                "3) Atores \n" + 
                "0) Sair\n");

                System.out.print("\nOpcao: ");
            
                try
                {
                    opcao = Integer.valueOf(scan.nextLine());
                } 
                catch (NumberFormatException e)
                {
                    opcao = -1;
                }

                switch (opcao)
                {
                    case 1:
                        new MenusSeries().menu();
                        break;
                    case 2:
                        new MenuEpisodios().menu();
                        break;
                    case 3:
                        System.out.println("Nao implementado\n");
                         break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                        break;
                }

            } while (opcao != 0);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
