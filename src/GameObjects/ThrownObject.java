/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public abstract class ThrownObject extends GameObject{
    
    private Node visualNode;
    private Node positionNode;
    private Vector3f start;
    private Vector3f destination;
    private float gravity;
    private float currentSpeedY;

    public ThrownObject(Node positionNode,Node visualNode, Vector3f start, Vector3f destination,float gravity,float currentSpeedY) {
        this.positionNode = positionNode;
        this.visualNode = visualNode;
        this.start = start;
        this.destination = destination;
        this.gravity = gravity;
        this.currentSpeedY = currentSpeedY;
    }

    public Node getVisualNode() {
        return visualNode;
    }

    public void setVisualNode(Node visualNode) {
        this.visualNode = visualNode;
    }

    public Node getPositionNode() {
        return positionNode;
    }

    public void setPositionNode(Node positionNode) {
        this.positionNode = positionNode;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getCurrentSpeedY() {
        return currentSpeedY;
    }

    public void setCurrentSpeedY(float currentSpeedY) {
        this.currentSpeedY = currentSpeedY;
    }
    
    

 

    public Vector3f getStart() {
        return start;
    }

    public void setStart(Vector3f start) {
        this.start = start;
    }

    public Vector3f getDestination() {
        return destination;
    }

    public void setDestination(Vector3f destination) {
        this.destination = destination;
    }
    
    
    
    
}
