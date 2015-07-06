package labos5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tijela3D {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("At least one file name should be provided");
		}

		ObjectModel model = readFromFile(args[0]);
		
		System.out.println(model.dumpToOBJ());

		// citanje zahtjeva korisnika
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			line = line.trim();
			switch (line) {
			case "normalize":
				model.normalize();
				System.out.println("Normalized object:");
				System.out.println(model.dumpToOBJ());
				break;
			case "quit":
				System.out.println("Terminating program");
				System.exit(1);
				break;
			default:
				String[] coordinates = line.split("\\s+");
				Vertex3D vertex = new Vertex3D(
						Double.parseDouble(coordinates[0]),
						Double.parseDouble(coordinates[1]),
						Double.parseDouble(coordinates[2]));
				int checkRelation = model.checkVertexStatus(vertex);
				if (checkRelation == 0) {
					System.out.println("Point on object surface!");
				} else if (checkRelation > 0) {
					System.out.println("Point outside object!");
				} else {
					System.out.println("Point inside object!");
				}
			}
		}
		in.close();
	}

	private static ObjectModel readFromFile(String fileName) {

		List<Vertex3D> vertices = new ArrayList<>();
		List<Face3D> faces = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(fileName)),
					"UTF-8"));

			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				if (line.isEmpty()) {
					continue;
				}
				line = line.trim();
				String[] data = line.split("\\s+");
				switch (data[0]) {
				case "v":
					vertices.add(new Vertex3D(Double.parseDouble(data[1]),
							Double.parseDouble(data[2]), Double
									.parseDouble(data[3])));
					break;
				case "f":
					faces.add(new Face3D(Integer.parseInt(data[1]) - 1, Integer
							.parseInt(data[2]) - 1,
							Integer.parseInt(data[3]) - 1));
					break;
				default:
					continue;
				}
			}

			reader.close();

		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding UTF-8 is not supported!");
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + "doesn't exist!");
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error reading file " + fileName);
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		}

		return new ObjectModel(vertices, faces);
	}

}
