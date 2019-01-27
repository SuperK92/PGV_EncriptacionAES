
package encriptacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Encriptacion {

    public static void main(String[] args) {
        String usuario;
        String pass;
        String mensaje = "Bienvenido";
        
        Scanner sc = new Scanner(System.in);
        
        String regexUsuario = "^[a-z]{8}$";
        String regexPass = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,16}$";
        
        do {
            System.out.println("Introduca el nombre: "); 
            usuario = sc.nextLine();  
            
            if(!validar(usuario, regexUsuario))
                System.out.println("El usuario debe tener una longitud de 8 caracteres en minúscula.");
                     
        } while(!validar(usuario, regexUsuario));
        
        do {
            System.out.println("Introduca la contraseña: "); 
            pass = sc.nextLine();
            
            if(!validar(pass, regexPass))
                System.out.println("La contraseña debe tener una longitud de entre 8-16 caracteres, una mayúscula, una minúscula y un número");
            
        } while(!validar(pass, regexPass));
       
        escribirFichero(usuario, pass, mensaje);
        
        
        
        
    }
    
    private static boolean validar(String nombre, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombre);
        
        if(matcher.find()) return true;
            
        return false;
    }
    
    private static void encriptar() throws NoSuchAlgorithmException, NoSuchPaddingException,
        BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        String plainText = "This is just an example";
        String algorithm = "Rijndael/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(algorithm);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypText = cipher.doFinal(plainText.getBytes());
    }
    
    private static void leerFichero() {
      File archivo = null;
      FileReader fr = null;
      BufferedReader br = null;

      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File ("info.txt");
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);

         // Lectura del fichero
         String linea;
         while((linea=br.readLine())!=null)
            System.out.println(linea);
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
   }
    
    private static void escribirFichero(String usuario, String pass, String mensaje) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("info.txt");
            pw = new PrintWriter(fichero);
            
            pw.println(usuario + ":" + pass + ":" + mensaje);
        

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }

    
}
