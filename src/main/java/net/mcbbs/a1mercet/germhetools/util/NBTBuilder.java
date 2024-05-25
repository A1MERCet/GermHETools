package net.mcbbs.a1mercet.germhetools.util;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NBTBuilder
{
    public final NBTTagCompound tag;

    public NBTBuilder()
    {
        this.tag = new NBTTagCompound();
    }


    public NBTBuilder(NBTTagCompound tag)
    {
        this.tag = tag ==null?new NBTTagCompound(): tag;
    }

    public NBTTagCompound get()                             {return tag;}
    public String getString(String k)                       {return tag.getString(k);}
    public int getInt(String k)                             {return tag.getInt(k);}
    public int[] getIntAry(String k)                        {return tag.getIntArray(k);}
    public float getFloat(String k)                         {return tag.getFloat(k);}
    public double getDouble(String k)                       {return tag.getDouble(k);}
    public long getLong(String k)                           {return tag.getLong(k);}
    public byte getByte(String k)                           {return tag.getByte(k);}
    public byte[] getByteAry(String k)                      {return tag.getByteArray(k);}
    public boolean getBoolean(String k)                     {return tag.getBoolean(k);}
    public short getShort(String k)                         {return tag.getShort(k);}
    public NBTBuilder getTagBuilder(String k)               {return new NBTBuilder(tag.getCompound(k));}
    public NBTTagCompound getTag(String k)                  {return tag.getCompound(k);}

    public NBTBuilder setTag(String k ,NBTBuilder tag)      {this.tag.set(k,tag.tag);return this;}
    public NBTBuilder setTag(String k ,NBTTagCompound tag)  {this.tag.set(k,tag);return this;}
    public NBTBuilder setString(String k ,String v)         {tag.setString(k,v);return this;}
    public NBTBuilder setInt(String k ,int v)               {tag.setInt(k,v);return this;}
    public NBTBuilder setIntAry(String k ,int[] v)          {tag.setIntArray(k,v);return this;}
    public NBTBuilder setFloat(String k ,float v)           {tag.setFloat(k,v);return this;}
    public NBTBuilder setDouble(String k ,double v)         {tag.setDouble(k,v);return this;}
    public NBTBuilder setLong(String k ,long v)             {tag.setLong(k,v);return this;}
    public NBTBuilder setByte(String k ,byte v)             {tag.setByte(k,v);return this;}
    public NBTBuilder setByteAry(String k ,byte[] v)        {tag.setByteArray(k,v);return this;}
    public NBTBuilder setBoolean(String k ,boolean v)       {tag.setBoolean(k,v);return this;}
    public NBTBuilder setShort(String k ,short v)           {tag.setShort(k,v);return this;}
}
