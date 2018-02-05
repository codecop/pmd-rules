package org.codecop.pmd.prototype.domain.base;

public class RelationShip<T extends Relatable> {

    public void some(T field) {
        assert field != null;
    }
}
