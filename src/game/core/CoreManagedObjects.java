package game.core;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;

/**
 * @author Michel Montenegro
 */
public class CoreManagedObjects 			
			implements Serializable, ManagedObject{

	private static final long serialVersionUID = -6198764068063530005L;

	//not in db ,only in memory
    private String objetcName;
    
    //not in db,only in memory
    private String objectDescription;

    
    /**
     * Creates a new {@code CoreVO object} with the given {@code name}
     * and {@code description}.
     *
     * @param name the name of this object
     * @param description the description of this object
     */
    public CoreManagedObjects(String objetcName, String objectDescription) {
        this.objetcName = objetcName;
        this.objectDescription = objectDescription;
    }

    /**
     * Sets the name of this object.
     *
     * @param name the name of this object
     */
    public void setObjectName(String objetcName) {
        AppContext.getDataManager().markForUpdate(this);
        this.objetcName = objetcName;
    }

    /**
     * Sets the description of this object.
     *
     * @param description the description of this object
     */
    public void setObjectDescription(String objectDescription) {
        AppContext.getDataManager().markForUpdate(this);
        this.objectDescription = objectDescription;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public String getObjectName() {
        return objetcName;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getObjectName();
    }
}
