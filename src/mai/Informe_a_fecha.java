package mai;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.Map;

public class Informe_a_fecha implements SQLData{

	private String tipoSQL = "INFORME_A_FECHA_T";
	
	private Date fecha;
	private int codigo;
	private Empleado empleado;
	
	
	public Informe_a_fecha() {	}
	public Informe_a_fecha(Date fecha, int codigo, Empleado empleado) {
		super();
		this.fecha = fecha;
		this.codigo = codigo;
		this.empleado = empleado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		return tipoSQL;
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		tipoSQL = typeName;
		setFecha(stream.readDate());
		setCodigo(stream.readInt());    
		
	    // Lee el REF del empleado
	    java.sql.Ref refEmpleado = (java.sql.Ref) stream.readRef();
	    
	    Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "system");

		 // Registrar el tipo personalizado en el typeMap
		 Map<String, Class<?>> typeMap = conexion.getTypeMap();
		 typeMap.put("EMPLEADO_T", Empleado.class); // Nombre del tipo SQL y la clase correspondiente
		 conexion.setTypeMap(typeMap);
	    
	    // Obtén el objeto Empleado desde la referencia
	    if (refEmpleado != null) {
	        Empleado empleado = (Empleado) refEmpleado.getObject(conexion.getTypeMap());
	        setEmpleado(empleado);
	    }
	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeDate(fecha);
		stream.writeInt(codigo);
		//stream.writeObject(empleado);
	}

}
