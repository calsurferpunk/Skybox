package com.example.skybox.util;


public class Geometry {
	
	public static class Point{
		public final float x,y,z;
		public Point(float x, float y, float z){
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Point translateY(float distance){
			return new Point(x, y+distance, z);
		}

		public Point translate(Vector vector) {
			// TODO Auto-generated method stub
			return new Point(x+vector.x, y+vector.y, z+vector.z);
		}
	}
	
	public static class Circle{
		public final Point center;
		public final float radius;
		
		public Circle(Point center, float radius) {
			// TODO Auto-generated constructor stub
			this.center = center;
			this.radius = radius;
		}
		
		public Circle scale(float scale){
			return new Circle(center, radius*scale);
		}
	}

	public static class Cylinder{
		public final Point center;
		public final float radius;
		public final float height;
		
		public Cylinder(Point center, float radius, float height) {
			// TODO Auto-generated constructor stub
			this.center = center;
			this.radius = radius;
			this.height = height;
		}
	}
	
	public static class Ray{
		public final Point point;
		public final Vector vector;
		
		public Ray(Point point, Vector vector) {
			// TODO Auto-generated constructor stub
			this.point = point;
			this.vector = vector;
		}
	}
	
	public static class Vector{
		public final float x,y,z;
		
		public Vector(float x, float y, float z) {
			// TODO Auto-generated constructor stub
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public float length() {
			// TODO Auto-generated method stub
			return (float)Math.sqrt(x*x+y*y+z*z);
		}

		/**
		 * Calculate the cross product of two vectors, that is, the length of the vector is twice the area of the triangle constructed by the two vectors
		 * @param other
		 * @return
		 */
		public Vector crossProduce(Vector other) {
			// TODO Auto-generated method stub
			return new Vector(
					(y*other.z) - (z*other.y), 
					(z*other.x) - (x*other.z),
					(x*other.y) - (y*other.x));
		}

		/**
		 * Dot product of two vectors
		 * @param other
		 * @return
		 */
		public float dotProduct(Vector other) {
			// TODO Auto-generated method stub
			return x*other.x
					+ y*other.y
					+ z*other.z;
		}

		/**
		 * Vector Scaling
		 * @param f
		 * @return
		 */
		public Vector scale(float f) {
			// TODO Auto-generated method stub
			return new Vector(x*f, y*f, z*f);
		}
	}
	
	public static class Sphere{
		public final Point center;
		public final float radius;
		
		public Sphere(Point center, float radius) {
			// TODO Auto-generated constructor stub
			this.center = center;
			this.radius = radius;
		}
	}

	public static Vector vectorBetween(Point from, Point to) {
		// TODO Auto-generated method stub
		return new Vector(to.x-from.x, to.y-from.y, to.z-from.z);
	}

	public static boolean intersects(Sphere sphere, Ray ray) {
		// TODO Auto-generated method stub
		return distanceBetween(sphere.center, ray) < sphere.radius;
	}

	private static float distanceBetween(Point point, Ray ray) {
		//The vectors from the start and end points of the ray to the center of the sphere
		Vector p1ToPoint = vectorBetween(ray.point, point);
		Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);
		//The above three points form a triangle, and the length of the cross product of the two vectors is twice the area of the triangle.
		float areaOfTriangleTimesTwo = p1ToPoint.crossProduce(p2ToPoint).length();
		float lengthOfBase = ray.vector.length();
		//Distance between ray and sphere = cross product of two vectors vector length / ray length
		return areaOfTriangleTimesTwo/lengthOfBase;
	}
	
	public static class Plane{
		public final Point point;
		public final Vector normal;
		
		public Plane(Point point, Vector normal) {
			// TODO Auto-generated constructor stub
			this.point = point;
			this.normal = normal;
		}
	}
	
	public static Point intersectionPoint(Ray ray, Plane plane){
		//Calculate the vector from the ray to the plane
		Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
		//Scaling factor = dot product between ray vector to plane and plane normal vector / dot product between ray vector and plane normal vector
		float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
				/ ray.vector.dotProduct(plane.normal);
		//Use the scaling factor to translate the ray points to find the intersection point
		Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
		return intersectionPoint;
	}
}
