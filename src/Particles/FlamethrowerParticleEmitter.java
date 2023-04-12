package Particles;

import com.jme3.bounding.BoundingBox;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;

public class FlamethrowerParticleEmitter extends ParticleEmitter {

    private transient Vector3f temp = new Vector3f();
    private transient Vector3f lastPos;
    private int firstUnUsed;

    private int lastUsed;

    public FlamethrowerParticleEmitter(String name, ParticleMesh.Type type, int numParticles) {
        super(name, type, numParticles);
    }

    private Particle emitParticle(Vector3f min, Vector3f max) {
        int idx = lastUsed + 1;
        
        if(idx >= getParticles().length){
            idx = 3000;
         lastUsed = 2999;   
        }
        
        
        if (idx >= getParticles().length) {
            return null;
        }

        Particle p = getParticles()[idx];
        if (this.isSelectRandomImage()) {
            p.imageIndex = FastMath.nextRandomInt(0, getImagesY() - 1) * getImagesX() + FastMath.nextRandomInt(0, getImagesX() - 1);
        }

        p.startlife = getLowLife() + FastMath.nextRandomFloat() * (getHighLife() - getLowLife());
        p.life = p.startlife;
        p.color.set(getStartColor());
        p.size = getStartSize();
        //shape.getRandomPoint(p.position);
        getParticleInfluencer().influenceParticle(p, getShape());
        if (isInWorldSpace()) {
            worldTransform.transformVector(p.position, p.position);
            worldTransform.getRotation().mult(p.velocity, p.velocity);
            // TODO: Make scale relevant somehow??
        }
        if (isRandomAngle()) {
            p.angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
        }
        if (getRotateSpeed() != 0) {
            p.rotateSpeed = getRotateSpeed() * (0.2f + (FastMath.nextRandomFloat() * 2f - 1f) * .8f);
        }

        temp.set(p.position).addLocal(p.size, p.size, p.size);
        max.maxLocal(temp);
        temp.set(p.position).subtractLocal(p.size, p.size, p.size);
        min.minLocal(temp);

        ++lastUsed;
        firstUnUsed = idx + 1;
        return p;
    }

    public Particle[] emitParticlesNum(int num) {
        // Force world transform to update
        this.getWorldTransform();

        TempVars vars = TempVars.get();

        BoundingBox bbox = (BoundingBox) this.getMesh().getBound();

        Vector3f min = vars.vect1;
        Vector3f max = vars.vect2;

        bbox.getMin(min);
        bbox.getMax(max);

        if (!Vector3f.isValidVector(min)) {
                        System.out.println(min);

            min.set(Vector3f.POSITIVE_INFINITY);
        }
        if (!Vector3f.isValidVector(max)) {
                        System.out.println("max: "+max);

            max.set(Vector3f.NEGATIVE_INFINITY);
        }

        ArrayList<Particle> particles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Particle p = emitParticle(min, max);
            if (p == null) {
                break;
            }
           particles.add(p);

        }

        bbox.setMinMax(min, max);
        this.setBoundRefresh();

        vars.release();
        
        
        return particles.toArray(new Particle[0]);
    }

    
    
    
    
    
    
    
    
    
}
