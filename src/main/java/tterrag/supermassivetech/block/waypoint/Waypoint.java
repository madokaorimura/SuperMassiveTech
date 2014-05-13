package tterrag.supermassivetech.block.waypoint;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.util.Color;

import com.google.common.collect.Sets;

public class Waypoint
{
    public static Set<Waypoint> waypoints = Sets.newConcurrentHashSet();
    
    public int x, y, z;
    private LinkedList<UUID> players;
    private boolean isempty = true;
    private Color color;
    
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
        
        Random rand = new Random();
        
        color = new Color(rand.nextInt(100), rand.nextInt(100), rand.nextInt(100));
        
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
            
            return this.x == wp.x && this.y == wp.y && this.z == wp.z;
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return (x + "," + y + "," + z + " " + (isNull() ? "" : players.toString())).hashCode();
    }
    
    public Color getColor()
    {
        return color;
    }
   
    public void writeToNBT(NBTTagCompound tag)
    {
        if (isNull()) return;
        
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
        
        tag.setByteArray("waypointcolor", new byte[]{color.getRedByte(), color.getGreenByte(), color.getBlueByte()});
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
        
        byte[] arr = tag.getByteArray("waypointcolor");

        if (arr.length < 2) return this;
        
        this.color = new Color(arr[0], arr[1], arr[2]);
        
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
