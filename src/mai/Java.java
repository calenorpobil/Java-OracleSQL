package mai;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class Java {
	public static void main(String[] args) {

		
		try {
			// Paso 2. Carga el driver JDBC
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// Paso 3. Identifico el origen de datos
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String usuario = "system";
			String passwd = "system";
			
			// Paso 4. Crea objeto Connection
			Connection conexion = DriverManager.getConnection(url,usuario,passwd);
			
			
			Statement sentencia = consultaMapeada(conexion);
			
			ResultSet res = consultaStruct(sentencia);
			
			
			
			
			
			res.close();		// Paso 8. Libera objeto ResultSet
			sentencia.close();	// Paso 9. Libera objeto Statement
			conexion.close();	// Paso 10. Libera objeto Connection
			
		} catch (ClassNotFoundException cn) {
			cn.printStackTrace();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Statement consultaMapeada(Connection conexion) throws SQLException, ClassNotFoundException {
		
		Map<String, Class<?>> typeMap = conexion.getTypeMap();

		typeMap.put("INFORME_A_FECHA_T", Informe_a_fecha.class);
		conexion.setTypeMap(typeMap);

		// Paso 5. Crea objeto Statement

		Statement sentencia = conexion.createStatement();
		
		String sql = "select * from conceptos_para_cv_ttab";

		ResultSet res = sentencia.executeQuery(sql);
		System.out.println("=======INFORMES POR MAPPING=======");
		while(res.next()) {
			java.sql.Ref ref = (java.sql.Ref) res.getObject(6);
			Informe_a_fecha iaf = 
					(Informe_a_fecha) ref.getObject(typeMap);
			
			int codigo = iaf.getCodigo();
			String fecha = iaf.getFecha().toString();
			String nombreEmp = iaf.getEmpleado().getNombre();
			ArrayList<String> listaTfnos = iaf.getEmpleado().
					getListaTelefonos();
			
			System.out.printf("Empleado %s: %s, añadido en %s, "
					+ "%nTeléfonos: %s%n%n", codigo, nombreEmp, fecha, listaTfnos.toString());

		}
		return sentencia;

		
	}

	private static ResultSet consultaStruct(Statement sentencia) throws SQLException {
		// Pasos 6 y 7. Ejecuta la consulta y recupera los datos en el ResulSet
		String sql = "select fecha, codigo, deref(i.emp) Nombre_de_empleado "
				+ "from informe_a_fecha_ttab i";
		ResultSet res = sentencia.executeQuery(sql);

		System.out.println("=======INFORMES POR STRUCT=======");
		// Consulta 1
		while (res.next() ) {
			System.out.printf("---------%n");
			System.out.println("Informe "+res.getString(2)+": ");
			java.sql.Struct jdbcStruct = (java.sql.Struct) res.getObject(3);
			Object[] atributos = jdbcStruct.getAttributes();
			
			System.out.printf("Nombre: %s %n", atributos[1].toString());
			//Casting para leer un VArray como un String[]: 
			java.sql.Array tfnos  = (java.sql.Array) atributos[2];
			String[] a = (String[]) tfnos.getArray();
			System.out.println("Teléfonos: ");
			for(int i=0; i<a.length; i++) {
				System.out.printf("%s ", a[i]);
			}
			System.out.printf("%nCalle: %s %n%n%n", atributos[3].toString());

		}
		return res;
		
	}
}
