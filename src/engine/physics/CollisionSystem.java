package engine.physics;

import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.CollisionData;
import engine.physics.collisions.IntersectionDetector;

import java.util.ArrayList;

public class CollisionSystem {

    private ArrayList<Collider> colliders;

    //region Getters & Setters

    public void addCollider(Collider collider) {
        colliders.add(collider);
    }

    public void clearColliders() {
        colliders.clear();
    }

    //endregion

    public CollisionSystem() {
        colliders = new ArrayList<>();
    }

    public void update() {


        for (int i = 0; i < colliders.size(); i++) {
            Collider a = colliders.get(i);
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider b = colliders.get(j);

                if (b.getAABB().getMin().x > a.getAABB().getMax().x) break;

                CollisionData data = new CollisionData();

                PhysicsBody aBody = (PhysicsBody) a.getParent();
                PhysicsBody bBody = (PhysicsBody) b.getParent();

                aBody.onGround(false);
                bBody.onGround(false);

                if (IntersectionDetector.aabbInAABB(a.getAABB(), b.getAABB(), data)) {
//                    System.out.println("Collision between: " + a + " and " + b);

                    Vector2 vAB = aBody.getVelocity().sub(bBody.getVelocity());
                    double vN = vAB.dot(data.normal);

                    final float epsilon = 0.0f;

                    float impulseForce = (float) ((float) (-(1 + epsilon) * vN) / (data.normal.dot(data.normal) * (aBody.getInverseMass()) + bBody.getInverseMass()));

                    Vector2 impulseVector = data.normal.mul(impulseForce);

                    Vector2 aVelocity = aBody.getVelocity();
                    Vector2 bVelocity = bBody.getVelocity();

                    Vector2 r1 = a.getAABB().getCenter().sub(data.point);
                    Vector2 r2 = b.getAABB().getCenter().sub(data.point);

                    double constraint = b.getAABB().getCenter().add(r2).sub(a.getAABB().getCenter()).sub(r1).dot(data.normal);
                    double constraintDiff = bVelocity.sub(aVelocity).dot(data.normal);

                    System.out.println(data.normal);

                    aBody.setVelocity(aVelocity.add(impulseVector.mul(aBody.getInverseMass())));
                    bBody.setVelocity(bVelocity.sub(impulseVector.mul(bBody.getInverseMass())));

                    aBody.setPosition(aBody.getPosition().sub(data.normal.mul(data.penetration).mul(aBody.getInverseMass())));
                    bBody.setPosition(bBody.getPosition().add(data.normal.mul(data.penetration).mul(bBody.getInverseMass())));

                    data.penetration = 0;

                    aBody.addForce(impulseVector.mul(aBody.getMass()));
                    bBody.addForce(impulseVector.mul(aBody.getMass()));


                }

            }
        }

    }

    public void sortByMinX() {
        int i = 1;
        while (i < this.colliders.size()) {
            int j = i;
            while (j > 0) {
                int aX = this.colliders.get(j - 1).getAABB().getMin().x;
                int bX = this.colliders.get(j).getAABB().getMin().x;

                if (aX > bX) {
                    Collider tmp = this.colliders.get(j - 1);
                    this.colliders.set(j - 1, this.colliders.get(j));
                    this.colliders.set(j, tmp);

                    j = j - 1;
                } else {
                    break;
                }
            }

            i = i + 1;
        }
    }



}