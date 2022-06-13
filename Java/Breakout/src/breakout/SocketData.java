package breakout;
import java.io.*;
import java.lang.*;

/**
 * Clase SocketData implementa Serializable
 */
public class SocketData implements Serializable
{
    /**
     * Constructor SocketData
     * @param cadena string
     */
    public SocketData(String cadena)
    {
        //Si la cadena no es null, se guarda la cadena y su longitud
        if (cadena != null)
        {
            c = cadena.length();
            d = cadena;
        }
    }

    /** Primer atributo */
    public Integer c =0;
    /** Segundo atributo */
    public String d = "";

    /** Metodo para devolver un String que represente el valor de todos los atributos. */
    public String toString()
    {
        String resultado;
        resultado = d;
        return resultado;
    }
    




    /**
     * Metodo para escribir los atributos de esta clase en un DataOutputStram de forma
     *  que luego pueda entenderlos un programa en C.
     * @param out outputstream
     */
    public void writeObject(java.io.DataOutputStream out) throws IOException
    {
        //Se envia la longitud de la cadena +1 por el \0 necesario en C
        out.writeInt(c+1);

        //Se envia una cadena como bytes.
        out.writeBytes(d);

        //Se envia el \0 del final de la cadena
        out.writeByte('\0');
    }


    /**
     * Metodo que lee los atributos de esta clase de un DataInputStream tal cual nos los
     * envia un programa en C.
     * No soporta que se envie un cadena "", es decir, un unico \0
     * @param in inputstream
     */
    public void readObject(java.io.DataInputStream in) throws IOException
    {
        //Se lee la longitud de la cadena y se le resta 1 para eliminar el \0 de C.
        c = in.readInt() - 1;

        //Array de bytes auxiliar para la lectura de la cadena.
        byte [] aux = null;

        aux = new byte[c];          //Se le da la longitud
        in.read(aux, 0, c);      //Se leen los bytes
        d = new String (aux);       //Se convierte a String
        in.read(aux ,0 ,1);  //Se lee el \0
    }

}
