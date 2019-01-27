
package encriptacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            String usuario;
            String pass;
            String mensaje;  
            
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
            
            mensaje = "Bienvenido " + usuario;
            
            byte[] usuarioEncriptado = encriptar(usuario);
            byte[] passEncriptado = encriptar(pass);
            byte[] mensajeEncriptado = encriptar(mensaje);
            escribirFichero(usuarioEncriptado, passEncriptado, mensajeEncriptado);
            
            String registro = leerFichero(usuarioEncriptado);
            
             String parts[] = registro.split(":");
             String usuarioReg = parts[0];
             String passReg = parts[1];
             String mensajeReg = parts[2];
             System.out.println(usuarioReg);
             System.out.println(passReg);
             System.out.println(mensajeReg);
             
            
            
            
            
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    private static boolean validar(String nombre, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombre);
        
        if(matcher.find()) return true;
            
        return false;
    }
    
    private static byte[] encriptar(String texto) throws NoSuchAlgorithmException, NoSuchPaddingException,
        BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        
        String algorithm = "Rijndael";
        String transf = "Rijndael/ECB/PKCS5Padding";
        
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        SecureRandom sr = new SecureRandom();
        String seed = "QuePenaQueSeAcabaronLasVacaciones";
        sr.setSeed(seed.getBytes());
        keyGen.init(128, sr);
        SecretKey secretKey = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance(transf);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
        byte[] encrypText = cipher.doFinal(texto.getBytes());
        
        return encrypText;
    }
    
    private static String desencriptar(String texto) throws NoSuchAlgorithmException, NoSuchPaddingException,
        BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        
        String algorithm = "Rijndael";
        String transf = "Rijndael/ECB/PKCS5Padding";
        
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        SecureRandom sr = new SecureRandom();
        String seed = "QuePenaQueSeAcabaronLasVacaciones";
        sr.setSeed(seed.getBytes());
        keyGen.init(128, sr);
        SecretKey secretKey = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance(transf);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        byte[] encrypText = cipher.doFinal(texto.getBytes());
        
        return new String(encrypText);
    }
    
    private static String leerFichero(byte[] usuarioEncriptado) {
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
         
         
         
         while((linea=br.readLine())!=null) {

               String parts[] = linea.split(":");
               String usuario = parts[0];
             if(usuarioEncriptado.toString().equals(usuario))
                 return linea;
         }
            
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
      return null;
   }
    
    private static void escribirFichero(byte[] usuario, byte[] pass, byte[] mensaje) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("info.txt", true);
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
