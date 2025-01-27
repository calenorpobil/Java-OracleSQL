package mai;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Empleado implements SQLData{
	
	private String nombre, dni, direccion;
	private int codigo;
	private ArrayList<String> listaTelefonos = new ArrayList<String>();
	
	private String tipoSQL = "EMPLEADO_T";

	public Empleado() {}
	public Empleado(int codigo, String nombre, ArrayList<String> listaTelefonos, String direccion,  String dni) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.dni = dni;
		this.direccion = direccion;
		this.listaTelefonos = listaTelefonos;
	}
	
	

	public int getCodigo() {
		return codigo;
	}



	public void setCodigo(int i) {
		this.codigo = i;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getDni() {
		return dni;
	}



	public void setDni(String dni) {
		this.dni = dni;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public ArrayList<String> getListaTelefonos() {
		return listaTelefonos;
	}



	public void setListaTelefonos(ArrayList<String> listaTelefonos) {
		this.listaTelefonos = listaTelefonos;
	}



	@Override
	public String getSQLTypeName() throws SQLException {
		return tipoSQL;
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		tipoSQL = typeName;
		setCodigo(stream.readInt());
		setNombre(stream.readString());	
		
        // Leer el VARRAY
        Array arrayTelefonos = stream.readArray();
        if (arrayTelefonos != null) {
            String[] objetos = (String[]) arrayTelefonos.getArray();
            this.listaTelefonos = new ArrayList<>();
            for (String obj : objetos) {
                this.listaTelefonos.add(obj);
            }
        }
		setDireccion(stream.readString());
		setDni(stream.readString());
	}

	
	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeInt(codigo);
		stream.writeString(nombre);
        // Convertir ArrayList<Integer> a SQL Array
        Object[] arrayTelefonos = listaTelefonos.toArray(new Integer[0]);
        Connection conexion = ConexionDB.getConexion();
        Array sqlArray = conexion.createArrayOf("NUMBER", arrayTelefonos);
        stream.writeArray(sqlArray);
		stream.writeString(direccion);
		stream.writeString(dni);
		
		
	}

}
