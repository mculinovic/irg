package labos5;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

import java.util.List;

public class ObjectModel {

	private Vertex3D[] vertices;
	private Face3D[] faces;

	private ObjectModel() {
	};

	public ObjectModel(List<Vertex3D> vertices, List<Face3D> faces) {
		this.vertices = vertices.toArray(new Vertex3D[0]);
		this.faces = faces.toArray(new Face3D[0]);

	}

	public ObjectModel copy() {
		ObjectModel copy = new ObjectModel();
		System.arraycopy(vertices, 0, copy.vertices, 0, vertices.length);
		System.arraycopy(faces, 0, copy.faces, 0, faces.length);
		return copy;
	}

	public String dumpToOBJ() {
		StringBuilder sb = new StringBuilder();
		for (Vertex3D v : vertices) {
			sb.append(v).append("\n");
		}
		for (Face3D f : faces) {
			sb.append(f).append("\n");
		}
		return sb.toString();
	}

	public void normalize() {

		if (faces.length == 0) {
			return;
		}

		double xmin;
		double xmax;
		double ymin;
		double ymax;
		double zmin;
		double zmax;
		xmin = xmax = vertices[faces[0].indexes[0]].x;
		ymin = ymax = vertices[faces[0].indexes[0]].y;
		zmin = zmax = vertices[faces[0].indexes[0]].z;

		for (Face3D f : faces) {
			for (int i : f.indexes) {
				xmin = Math.min(xmin, vertices[i].x);
				xmax = Math.max(xmax, vertices[i].x);
				ymin = Math.min(ymin, vertices[i].y);
				ymax = Math.max(ymax, vertices[i].y);
				zmin = Math.min(zmin, vertices[i].z);
				zmax = Math.max(zmax, vertices[i].z);
			}
		}

		double M = Math.max(xmax - xmin, ymax - ymin);
		M = Math.max(M, zmax - zmin);

		double cx = (xmax + xmin) / 2;
		double cy = (ymax + ymin) / 2;
		double cz = (zmax + zmin) / 2;

		for (int i = 0; i < vertices.length; i++) {
			double x = vertices[i].x - cx;
			double y = vertices[i].y - cy;
			double z = vertices[i].z - cz;

			vertices[i] = new Vertex3D(x * 2 / M, y * 2 / M, z * 2 / M);
		}

		for (Face3D f : faces) {
			Vertex3D v = vertices[f.indexes[0]];
			Vertex3D v1 = vertices[f.indexes[1]];
			Vertex3D v2 = vertices[f.indexes[2]];

			double x1 = v1.x - v.x;
			double x2 = v2.x - v.x;
			double y1 = v1.y - v.y;
			double y2 = v2.y - v.y;
			double z1 = v1.z - v.z;
			double z2 = v2.z - v.z;

			// koeficijenti ravnine
			double a = y1 * z2 - y2 * z1;
			double b = -(x1 * z2 - x2 * z1);
			double c = x1 * y2 - x2 * y1;
			double d = -a * v.x - b * v.y - c * v.z;
			f.setA(a);
			f.setB(b);
			f.setC(c);
			f.setD(d);
		}

	}

	public void izracunajNormaleVrhova() {

		for (Face3D face : faces) {
			for (int i = 0; i < face.indexes.length; i++) {
				vertices[face.indexes[i]].dodajNormal(face.getA(), face.getB(),
						face.getC());
			}
		}
		
		for (Vertex3D v : vertices) {
			v.izracunajNormaluVrha();
		}
	}

	public int checkVertexStatus(Vertex3D vertex) {

		@SuppressWarnings("unused")
		int below = 0;
		int on = 0;

		for (Face3D f : faces) {
			Vertex3D v = vertices[f.indexes[0]];
			Vertex3D v1 = vertices[f.indexes[1]];
			Vertex3D v2 = vertices[f.indexes[2]];

			double x1 = v1.x - v.x;
			double x2 = v2.x - v.x;
			double y1 = v1.y - v.y;
			double y2 = v2.y - v.y;
			double z1 = v1.z - v.z;
			double z2 = v2.z - v.z;

			// koeficijenti ravnine
			double a = y1 * z2 - y2 * z1;
			double b = -(x1 * z2 - x2 * z1);
			double c = x1 * y2 - x2 * y1;
			double d = -a * v.x - b * v.y - c * v.z;

			// provjeri odnos tocke i ravnine
			double result = a * vertex.x + b * vertex.y + c * vertex.z + d;
			if (result == 0) {
				on++;
			} else if (result < 0) {
				below++;
			} else {
				return 1;
			}
		}

		if (on > 0) {
			return 0;
		} else {
			return -1;
		}
	}

	public Vertex3D[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex3D[] vertices) {
		this.vertices = vertices;
	}

	public Face3D[] getFaces() {
		return faces;
	}

	public void setFaces(Face3D[] faces) {
		this.faces = faces;
	}

	public void determineFaceVisibilities1(IVector eye) {

		double eyeX = eye.get(0);
		double eyeY = eye.get(1);
		double eyeZ = eye.get(2);

		for (Face3D f : faces) {
			if (f.getA() * eyeX + f.getB() * eyeY + f.getC() * eyeZ + f.getD() > 0) {
				f.setVisible(true);
			} else {
				f.setVisible(false);
			}
		}
	}

	public void determineFaceVisibilities2(IVector eye) {

		for (Face3D f : faces) {
			int[] indexes = f.indexes;
			Vertex3D v0 = vertices[indexes[0]];
			IVector vi0 = new Vector(v0.x, v0.y, v0.z);
			Vertex3D v1 = vertices[indexes[1]];
			IVector vi1 = new Vector(v1.x, v1.y, v1.z);
			Vertex3D v2 = vertices[indexes[2]];
			IVector vi2 = new Vector(v2.x, v2.y, v2.z);
			IVector c = null;
			try {
				c = vi0.nAdd(vi1).nAdd(vi2).scalarMultiply(1.0 / 3.0);
			} catch (IncompatibleOperandException e) {
				e.printStackTrace();
			}

			IVector e = null;
			try {
				e = eye.nSub(c);
			} catch (IncompatibleOperandException ex) {
				ex.printStackTrace();
			}

			IVector n = new Vector(f.getA(), f.getB(), f.getC());

			try {
				if (n.scalarProduct(e) >= 0) {
					f.setVisible(true);
				} else {
					f.setVisible(false);
				}
			} catch (IncompatibleOperandException e1) {
				e1.printStackTrace();
			}

		}
	}

}
