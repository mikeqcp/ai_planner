(define (problem pb6)
	(:domain travel)
	(:requirements :strips :equality)
  	(:objects a b d e g jack)
  	(:init (at jack a) 
	 	(mobile jack)
	 	(person jack)
	 	(road a e)
	 	(road e b)
	 	(bridge b d)
	 	(road d g))
	(:goal (and (at Jack g)))
)