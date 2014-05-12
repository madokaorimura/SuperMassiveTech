package tterrag.supermassivetech.block.waypoint;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Waypoint
{
    public static Set<Waypoint> waypoints = new HashSet<Waypoint>();
    
    private int x, y, z;
    private LinkedList<UUID> players;
    private boolean isempty = true;
    
    public Waypoint(){}
    
    public Waypoint(int x, int y, int z, EntityPlayer... players)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.players = new LinkedList<UUID>();
        
        for (EntityPlayer e : players)
        {
            this.players.add(e.getUniqueID());
        }
        
        this.isempty = false;
    }
    
    public Waypoint addPlayer(EntityPlayer player)
    {
        this.players.add(player.getUniqueID());
        return this;
    }
    
    public Waypoint removePlayer(EntityPlayer player)
    {
        this.players.remove(player.getUniqueID());
        return this;
    }
    
    public boolean viewableBy(EntityPlayer player)
    {
        return players.contains(player.getUniqueID());
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Waypoint)
        {
            Waypoint wp = (Waypoint) obj;
            
            return this.x == wp.x && this.y == wp.y && this.y == wp.z  && containsAny(wp.players);
        }
        
        return false;
    }
    
    private boolean containsAny(Collection<UUID> list)
    {
        for (UUID uuid : list)
        {
            if (players.contains(uuid))
                return true;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return (x + "," + y + "," + z + " " + (isNull() ? "" : players.toString())).hashCode();
    }
    
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("waypointx", x);
        tag.setInteger("waypointy", y);
        tag.setInteger("waypointz", z);
        
        UUID[] uuids = players.toArray(new UUID[]{});
        int[] uuidnums = new int[uuids.length * 4];
        
        for (int i = 0; i < uuids.length; i++)
        {
            long msd = uuids[i].getMostSignificantBits();
            long lsd = uuids[i].getLeastSignificantBits();
            
            uuidnums[i * 4] = (int) (msd >> 32);
            uuidnums[i * 4 + 1] = (int) msd;
            uuidnums[i * 4 + 2] = (int) (lsd >> 32);
            uuidnums[i * 4 + 3] = (int) lsd;
        }
        
        tag.setIntArray("playeruuids", uuidnums);
    }
    
    public Waypoint readFromNBT(NBTTagCompound tag)
    {
        this.x = tag.getInteger("waypointx");
        this.y = tag.getInteger("waypointy");
        this.z = tag.getInteger("waypointz");
        
        int[] uuidnums = tag.getIntArray("playeruuids");
        UUID[] uuids = new UUID[uuidnums.length / 4];
        
        for (int i = 0; i < uuidnums.length; i += 4)
        {
            long msd = ((long) uuidnums[i]) << 32;
            msd += uuidnums[i + 1];
            long lsd = ((long) uuidnums[i + 2]) << 32;
            lsd += uuidnums[i + 3];
            
            uuids[i / 4] = new UUID(msd, lsd);
        }
        
        this.players = new LinkedList<UUID>(Arrays.asList(uuids));
        
        this.isempty = false;
        
        return this;
    }
    
    public boolean isNull()
    {
        return isempty;
    }
    
    @Override
    public String toString()
    {
        return String.format("x: %d, y: %d, z: %d   %s", x, y, z, !isNull() ? Arrays.deepToString(players.toArray(new UUID[]{})) : "");
    }
}
