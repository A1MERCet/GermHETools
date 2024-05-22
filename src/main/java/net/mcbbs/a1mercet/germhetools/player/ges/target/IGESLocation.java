package net.mcbbs.a1mercet.germhetools.player.ges.target;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface IGESLocation extends IGESTarget
{
    Location getLocation();

    void setLocation(Location loc);

    void updateLocation(Location location);

    default void updateLocation(){updateLocation(getLocation());}

    void remove();

    void setOffset(Vector vector);

    Vector getOffset();

    void setRotate(Vector vector);

    Vector getRotate();

    void setScale(Vector vector);

    Vector getScale();
}
