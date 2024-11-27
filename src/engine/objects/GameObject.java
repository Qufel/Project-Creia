package engine.objects;

import engine.physics.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class GameObject {

    private String name; // Unique name identifier inside parent
    private GameObject parent; // Parent. Should be null only for Scene
    private ArrayList<GameObject> children = new ArrayList<GameObject>();

    private Vector2 position;
    private Vector2 globalPosition;

    private boolean visible = true;

    //region Getters & Setters

    public void setPosition(Vector2 position) {
        this.position = position;

        if(this instanceof Scene) {
            globalPosition  = new Vector2(position.x, position.y);
        } else {
            globalPosition = parent.getGlobalPosition().add(new Vector2(position.x, -position.y));
        }

        for (GameObject child : children) {
            child.setPosition(child.getPosition());
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getGlobalPosition() {
        return globalPosition;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    //endregion

    public GameObject(GameObject parent, String name, Vector2 position) {

        if (parent == null && !(this instanceof Scene)) {
            throw new NullPointerException("Parent is null");
        }

        this.parent = parent;

        if (!(this instanceof Scene)) {
            parent.addChildren(this);
        }

        this.name = name;
        this.setPosition(position);
    }

    @Override
    public String toString() {
        ArrayList<String> names = new ArrayList<>();
        names.add(name);

        GameObject p = this.parent;
        while (p != null) {
            names.addFirst(p.name);
            p = p.getParent();
        }

        return String.join("/", names);
    }

    public void changeParent(GameObject newParent) {
        if (newParent != null) {
            this.parent = newParent;
        }
    }

    public void decompose() {
        for (GameObject child : children) {
            child.decompose();
            child = null;
            children.remove(child);
        }
    }

    //region Children/Parent management

    /**
     * Returns a parent of this object
     * @return
     */
    public GameObject getParent() {
        return parent;
    }

    /**
     * Adds child (or children) to this object.
     * @param children object/objects to be added to the children list.
     */
    public void addChildren(GameObject ...children) {

        for (GameObject child : children) {
            if (this.children.contains(child)) {
                this.removeChild(child);
            }
        }

        this.children.addAll(Arrays.asList(children));
    }

    /**
     * Returns a children list.
     * @return children
     */
    public ArrayList<GameObject> getChildren() {
        return children;
    }

    /**
     * Removes a child from this object.
     * @param child an object inside the children list.
     */
    public void removeChild(GameObject child) {
        this.children.remove(child);
    }

    /**
     * Returns a first child in this object by the index inside th children list.
     * @param index an index of child inside this object.
     * @return child | null
     */
    public GameObject getChild(int index) {
        if( index < 0 || index >= this.children.size() )
            return null;

        return this.children.get(index);
    }

    /**
     * Returns a first child of an object of a given name.
     * @param name a name of a child inside the object.
     * @return child | null
     */
    public GameObject getChild(String name) {
        return this.children.stream().filter(obj -> obj.name == name).findFirst().orElse(null);
    }

    //endregion
}
