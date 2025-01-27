package mai;

import java.sql.Array;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class ListaTelefonos implements SQLData{

	private String tipoSQL = "LISTATFNOS_TVAR";

	private Array nombre;
	
	
	
	public ListaTelefonos() {}
	public ListaTelefonos(String tipoSQL, Array nombre) {
		super();
		this.tipoSQL = tipoSQL;
		this.nombre = nombre;
	}

	public Array getNombre() {
		return nombre;
	}

	public void setNombre(Array array) {
		this.nombre = array;
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		// TODO Auto-generated method stub
		return tipoSQL;
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		tipoSQL = typeName;
		setNombre(stream.readArray());		
	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeArray(nombre);
	}

}
