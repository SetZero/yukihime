����   3  kuroimori/Yukihime  robocode/AdvancedRobot enemypos [D prevEnemyPos steps I ConstantValue    enemyTarget enemycountercampshots bulletStrength D <init> ()V Code
    ��      	    	    		    		     LineNumberTable LocalVariableTable this Lkuroimori/Yukihime; setup
  % & ' setAdjustGunForRobotTurn (Z)V
  ) * ' setAdjustRadarForGunTurn
  , - ' setAdjustRadarForRobotTurn
  / 0 1 
getHeading ()D
 3 5 4 robocode/util/Utils 6 7 normalRelativeAngleDegrees (D)D
  9 : ; 	turnRight (D)V
  = >  execute getRelativePosition  (Lrobocode/ScannedRobotEvent;)[D
 B D C robocode/ScannedRobotEvent E 1 getDistance
 B G H 1 getBearingRadians
  J K 1 getHeadingRadians
 M O N java/lang/Math P 7 sin
 M R S 7 cos enemy Lrobocode/ScannedRobotEvent; distance bearing vectorXEnemy vectorYEnemy coordinates getAbsolutePosition
  ] ? @
  _ ` 1 getX
  b c 1 getY isMoving ([D)Z
 M g h i round (D)J	  k l m out Ljava/io/PrintStream;
 o q p java/io/PrintStream r s println (J)V u Error in isMoving
 o w r x (Ljava/lang/String;)V enemyPos prevX J prevY enemX enemY StackMapTable  angleMoveTo ([D)V@       
  � � ; ahead@V�     
  � � ; turnLeft i turnRadarToEnemy (Lrobocode/ScannedRobotEvent;)V
  � � 1 getRadarHeadingRadians
  � � ; setTurnRadarRightRadians e 	currradar 
shootEnemy@4      @      
 B � � 1 getVelocity
 B J
  � [ @
  � � 1 getBattleFieldWidth
  � � 1 getBattleFieldHeight
 M � � � atan2 (DD)D
  � � 1 getGunHeadingRadians@	!�TD-
 3 � � 7 normalRelativeAngle � java/lang/StringBuilder � TurnBy: 
 � �  x
 � � � � append (D)Ljava/lang/StringBuilder;
 � � � � toString ()Ljava/lang/String;
  � � ; setTurnGunRightRadians
  � � ; setFire bulletVelocity enemydis enemyVelocity enemyHeading 
velocity_x 
velocity_y timeConsume 	newenemyx 	newenemyy turn newpos run
  � # �      
  � � ; turnRadarRightRadians	  �  
  � � � onScannedRobot
  � � 1 	getEnergy@9      
 B �
  � d e
  � � @	  �   	radarTurn onHitByBullet (Lrobocode/HitByBulletEvent;)V � java/util/Random
 � 
 � � � � nextInt (I)I event Lrobocode/HitByBulletEvent; rand Ljava/util/Random; onPaint (Ljava/awt/Graphics2D;)V
 �  � java/awt/Graphics2D drawRect (IIII)V g Ljava/awt/Graphics2D; 
SourceFile Yukihime.java !                  	  
            	                f     (*� *�Y RY R� *� *
� *� �                  "  '          ( ! "    #      _     !*� $*� (*� +**� .g� 2� 8*� <�           !  " 
 #  )  +   ,         ! ! "    ? @     �     9+� AI+� F9*� Ic� L(k9*� Ic� Q(k9�YRYR:

�           6  7  9  : ' < 6 >     H    9 ! "     9 T U   4 V    . W      X   '  Y   6  Z  
  [ @     h     *+� \M,,1*� ^cR,,1*� acR,�           H  I  J  L           ! "      T U    Z    d e    %  
   t*� 1� fA*� 1� f7+1� f7+1� f7*� j� n 	�� 
	�� 
*+� � �� �� 
*+� � �� �� �*� jt� v�       >    Y 
 Z  [  \ % ^ . ` ; b @ c B e Q g V h X j g l i o r p     >    t ! "     t y   
 j z {   _ | {   W } {  % O ~ {      � ;   �    � �     �     2=� **+1 �o� �* �� 8*+1 �o� �* �� ����ױ           �  �  �  � " � ) � 1 �          2 ! "     2 Z    / � 	      � &  � �     Q     *� �I*� ��           �  � 
 �           ! "      � U    �    � @         � � �*� kgI+� A9+� �9+� �9� Lk9
� Qk9(o9*+� �:1
kc91kc9*� ��� *� �9� �� 9*� ��� *� �9� �� 9*� ^g*� ag� �*� �g �g� �9*� j� �Y�� �� �� �� v*� �**� � ��YRYR:�       j    �  �  �  �  � ) � 3 � 9 � @ � L � X � b � h � k � r � u �  � � � � � � � � � � � � � � � � � � �     �    � ! "     � � U   � �    � �    � �    � �   ) � �  
 3 � �   9 � �   @ � y   L � �   X � �   � 8 �   �  �       � k   B �  		  �      i     "*� �* ׶ �**� ܶ �*� <**� ܶ ާ��           �  �  �  �  �  �         " ! "         � �         t*� I+� Fc*� �gI*(� �� �**+� \� �*� � 䗝 +� �*� ᘜ ?**+� �� � %* �� 6� *+� �W�*� ��� *� **+� � �       6    �  �  �   � 7 � C � J � P � V � b � e � j � s �     *    t ! "     t � U   e �   M  � 	      � 7� �   � �     s     )� �Y� �M*� �,*� ��� �R*� �,*� ��� �R�           �  �  � ( �          ) ! "     ) � �   ! � �   � �     U     +*� �1�d*� �1�d((� ��       
    �            ! "          