package ejercicio11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Gestor11 {
	Scanner sc = new Scanner(System.in);
	Alumno a1 = new Alumno();
	ArrayList<Alumno> aLista = new ArrayList<>();

	
	public void menu() {
		int opcion;
		do {
			System.out.println("Instertar una opcion:\n" 
					+ "1. Insertar alumno\n" 
					+ "2. Mostrar todos los alumnnos\n"
					+ "3. Guardar todos los alumnos en un fichero (txt o dat)\n"
					+ "4. Leer alumnos de un fichero (con el formato anterior), y guardarlo en una BBDD\n"
					+ "5. Modificar el nombre de un alumno guardado en la base de datos a partir de su Primary Key\n"
					+ "6. Eliminar un alumno a partir de su (PK)\n"
					+ "7. Eliminar los alumnos que su apellido contengan la palabra dada por el usuario.\n"
					+ "8. Guardar todos los alumnos en un fichero XML o JSON.\n"
					+ "9. Leer un fichero XML o JSON de alumnos y guardarlos en la BD.");
			opcion = sc.nextInt();

			switch (opcion) {
			case 1:

				break;
			case 2:
				mostrarDatos();
				break;
			case 3:
				guardarEnFichero();
				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;

			default:
				break;
			}
		} while (opcion != 0);
	}
	
	public Connection conexion() {
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conexion=DriverManager.getConnection("jdbc:mysql://local/host/Alumnos25","root","manager");
			return conexion;

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conexion;
		
	}
	
	
 	public void datosAlumno() {
		System.out.println("Introduzca los siguientes datos:");

		System.out.println("Intoduzca el NIA");
		a1.setNia(sc.nextInt());

		sc.nextLine();

		System.out.println("Intoduzca el Nombre");
		a1.setNombre(sc.nextLine());

		System.out.println("Intoduzca el Apellidos");
		a1.setApellidos(sc.nextLine());

		System.out.println("Intoduzca el Genero");

		String genero = sc.nextLine();

		a1.setGenero(genero.charAt(0));

		System.out.println("Intoduzca el Fecha de nacimiento dd/mm/yyyy");

		String fecha = sc.nextLine();

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		try {
			a1.setFechaNac(formato.parse(fecha));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Intoduzca el Ciclo");
		a1.setCiclo(sc.nextLine());

		System.out.println("Intoduzca el Curso");
		a1.setCurso(sc.nextLine());

		System.out.println("Intoduzca el Grupo");
		a1.setGrupo(sc.nextLine());

		aLista.add(a1);

	}

	public void mostrarDatos() {

		for (Alumno alumno : aLista) {
			System.out.println(alumno);
		}

	}
	
	public void guardarEnFichero() {
		String ruta;
		System.out.println("Introduzca la ruta");
		ruta=sc.nextLine();
		
		try(ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(ruta)) ) {
			
			oos.writeObject(aLista);
			
		} catch (Exception e) {
		}
	}
	
	public void leerFichero() {
		String ruta;
		System.out.println("Introduzca la ruta");
		ruta=sc.nextLine();
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))){
			
			a1=(Alumno) ois.readObject();
			aLista.add(a1);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void modificarNombrePK() {
		
	}
	
	public void eliminarAlumnoPK() {
		
	}
	
	public void eliminarPorApellido() {
		
	}
	
	public void guardarFicheroJSON() {
		
	}
	public void guardarFicheroXML() {
		
	}
}
