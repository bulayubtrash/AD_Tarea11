package ejercicio11;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Gestor11 {
	Scanner sc = new Scanner(System.in);
	Alumno a1;
	ArrayList<Alumno> aLista = new ArrayList<>();

	private static final String URL = "jdbc:mysql://localhost:3306/alumno25";
	private static final String USER = "root";
	private static final String PASSWORD = "manager1";

	public void menu() {
		int opcion;
		do {
			System.out.println("Instertar una opcion:\n" + "1. Insertar alumno\n" + "2. Mostrar todos los alumnnos\n"
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
				datosAlumno();
				break;
			case 2:
				mostrarDatos();
				break;
			case 3:
				guardarEnFichero();
				break;
			case 4:
				leerFichero();
				break;
			case 5:
				modificarNombrePK();
				break;
			case 6:
				eliminarAlumnoPK();
				break;
			case 7:
				eliminarPorApellido();
				break;
			case 8:
				escribirJSON(aLista);
				break;
			case 9:
				leerJSON();
				break;

			default:
				break;
			}
		} while (opcion != 0);
	}

	public void insertarAlumno(Alumno a1) {

		String sql = "INSERT INTO alumnos (nia, nombre, apellido, genero, fechaNac, ciclo, curso, grupo) VALUES (? ,?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(0, a1.getNia());
			ps.setString(1, a1.getNombre());
			ps.setString(2, a1.getApellidos());

			String genChar = String.valueOf(a1.getGenero());
			ps.setString(3, genChar);

			Date fechaAux = (Date) a1.getFechaNac();
			ps.setDate(4, fechaAux);

			ps.setString(5, a1.getCiclo());
			ps.setString(6, a1.getCurso());
			ps.setString(7, a1.getGrupo());

			int filasInsertadas = ps.executeUpdate();
			if (filasInsertadas > 0) {
				System.out.println("Alumno agregado");
			}

		} catch (SQLException e) {
			// TODO: handle exception
		}

	}

	public void datosAlumno() {
		a1 = new Alumno();

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

		insertarAlumno(a1);

	}

	public void mostrarDatos() {
		a1 = new Alumno();
		ArrayList<Alumno> listaAux = new ArrayList<>();

		String sql = "SELECT * FROM alumnos";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				a1.setNia(rs.getInt("nia"));
				a1.setNombre(rs.getString("nombre"));
				a1.setApellidos(rs.getString("apellido"));

				char genAux = rs.getString("genero").charAt(0);
				a1.setGenero(genAux);

				Date fechaUtil = rs.getDate("fechaNac");
				a1.setFechaNac(fechaUtil);

				a1.setCiclo(rs.getString("ciclo"));
				a1.setCurso(rs.getString("curso"));
				a1.setGrupo(rs.getString("grupo"));

				listaAux.add(a1);
			}

			for (Alumno alumno : listaAux) {
				System.out.println(alumno);
			}

		} catch (SQLException e) {
			// TODO: handle exception
		}

	}

	public void guardarEnFichero() {
		String ruta;
		System.out.println("Introduzca la ruta");
		ruta = sc.nextLine();

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {

			oos.writeObject(aLista);

		} catch (Exception e) {
		}
	}

	public void leerFichero() {
		String ruta;
		System.out.println("Introduzca la ruta");
		ruta = sc.nextLine();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {

			a1 = (Alumno) ois.readObject();
			aLista.add(a1);

			insertarAlumno(a1);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void modificarNombrePK() {
		System.out.println("Indicar el nia del alumno");
		int nia = sc.nextInt();
		System.out.println("Introducir nuevo nombre");
		String nombre = sc.nextLine();

		String sql = "UPDATE alumnos SET nombre = ? WHERE nia = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, nombre);
			ps.setInt(2, nia);

			int filasActualizadas = ps.executeUpdate();
			if (filasActualizadas > 0) {
				System.out.println("El nombre del alumno ha sido actualizado correctamente.");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void eliminarAlumnoPK() {
		System.out.println("Indicar el nia del alumno");
		int nia = sc.nextInt();

		String sql = "DELETE FROM alumnos WHERE nia = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setInt(0, nia);

			int filasActualizadas = ps.executeUpdate();
			if (filasActualizadas > 0) {
				System.out.println("El alumno ha sido eliminado");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void eliminarPorApellido() {
		System.out.println("Indicar el apellido del alumno");
		String apellidos=sc.nextLine();
		String sql = "DELETE FROM alumnos WHERE apellido = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(0, apellidos);

			int filasActualizadas = ps.executeUpdate();
			if (filasActualizadas > 0) {
				System.out.println("El alumno ha sido eliminado");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void escribirJSON(ArrayList<Alumno>aLista) {
		String ruta;
		System.out.println("Introduzca la ruta");
		ruta = sc.nextLine();
		
		Gson gson = new Gson();

		try(FileWriter writer = new FileWriter(ruta);) {
			
			gson.toJson(aLista, writer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void leerJSON() {
		ArrayList<Alumno> listaAux = new ArrayList<>();

		String ruta;
		System.out.println("Introduzca la ruta");
		ruta = sc.nextLine();
		Gson gson= new Gson();
		
		try(FileReader reader = new FileReader(ruta);) {
			
            // Definir el tipo de la lista de alumnos usando TypeToken
			
            Type alumnosListType = new TypeToken<List<Alumno>>(){}.getType();
            
            // Leer el JSON y convertirlo en una lista de Alumno
            
            listaAux = gson.fromJson(reader, alumnosListType);
            
            // Inicializar alumnos si el archivo está vacío
            
            if (listaAux == null) {
            	listaAux = new ArrayList<>();
            }
            for (Alumno alumno : listaAux) {
				System.out.println(alumno);
				insertarAlumno(alumno);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
