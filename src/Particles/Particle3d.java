/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Particles;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public class Particle3d {
   
    private Vector3f direction;
    private boolean isRising;
    private Node node;
    private Vector3f flyDirection;
    private float fallSpeed;
    private float rotationSpeed;
    
    public Particle3d(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection) {
            this.node =node;
        this.direction = direction;
        this.isRising = isRising;
        this.flyDirection = flyDirection;
        this.fallSpeed = 12;
        this.rotationSpeed = 3;
    }
    
      public Particle3d(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection, float fallSpeed) {
            this.node =node;
        this.direction = direction;
        this.isRising = isRising;
        this.flyDirection = flyDirection;
        this.fallSpeed = fallSpeed;
    }
    
    public Particle3d(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection, float fallSpeed, float rotationSpeed) {
            this.node =node;
        this.direction = direction;
        this.isRising = isRising;
        this.flyDirection = flyDirection;
        this.fallSpeed = fallSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public boolean isIsRising() {
        return isRising;
    }

    public void setIsRising(boolean isRising) {
        this.isRising = isRising;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Vector3f getFlyDirection() {
        return flyDirection;
    }

    public void setFlyDirection(Vector3f flyDirection) {
        this.flyDirection = flyDirection;
    }

    public float getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(float fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
    
    
    
}
