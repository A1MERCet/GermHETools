package net.mcbbs.a1mercet.germhetools.util;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Options<K> extends HashMap<K, Options.IValue> implements Serializable , IConfig
{
    @Override public String getDefaultPath() {return "Options";}
    @Override
    public void save(ConfigurationSection section) {
        if(section==null){return;}
        for(K k : keySet())
            try {
                String stringK = toStringKey(k);
                IValue value = get(k);
                section.set(stringK+".Value",value.getValue().toString());
                section.set(stringK+".Type",value.getValueType().name());
            }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void load(ConfigurationSection section)
    {
        if(section==null){return;}
        Set<String> set = section.getKeys(false);
        if(set!=null)
            for(String stringK : set)
                try {
                    K k = transformKey(stringK);
                    String valueString = section.getString(stringK+".Value");
                    Type type = Type.valueOf(section.getString(stringK+".Type","OBJECT"));
                    put(k,type.create(valueString));
                }catch (Exception e){e.printStackTrace();}
    }

    public K transformKey(String k) {return (K)k;}
    public String toStringKey(K k)  {return k.toString();}

    public enum Type
    {
        STRING  {@Override public ValueString create(Object o)  {ValueString v  = new ValueString();    v.setValue(o);return v;}},
        BOOLEAN {@Override public ValueBoolean create(Object o) {ValueBoolean v = new ValueBoolean();   v.setValue(o);return v;}},
        INT     {@Override public ValueInt create(Object o)     {ValueInt v     = new ValueInt();       v.setValue(o);return v;}},
        FLOAT   {@Override public ValueFloat create(Object o)   {ValueFloat v   = new ValueFloat();     v.setValue(o);return v;}},
        DOUBLE  {@Override public ValueDouble create(Object o)  {ValueDouble v  = new ValueDouble();    v.setValue(o);return v;}},
        LONG    {@Override public ValueLong create(Object o)    {ValueLong v    = new ValueLong();      v.setValue(o);return v;}},
        OBJECT  {@Override public Value create(Object o)        {Value v        = new Value();          v.setValue(o);return v;}},
        VECTOR  {@Override public ValueVector create(Object o)  {ValueVector v  = new ValueVector();    v.setValue(o);return v;}},
        ;
        public IValue create(Object o){return new Value().setValue(o);}
    }
    public interface INumber{}
    public interface IValue
    {
        Object getValue();
        Value setValue(Object o);
        String toString();
        Class<?> getType();
        Type getValueType();
    }
    public static class Value implements IValue
    {
        protected Object value;

        @Override public Object getValue() {return value;}
        @Override public Value setValue(Object o){this.value= o;return this;}
        @Override public String toString() {return value==null?null:value.toString();}
        @Override public Class<?> getType(){return value.getClass();}
        @Override public Type getValueType(){return Type.OBJECT;}

    }
    public static class ValueString extends Value
    {
        @Override
        public String getValue()
        {
            return value==null?null:(String) value;
        }
        @Override public Type getValueType(){return Type.STRING;}
    }
    public static class ValueBoolean extends Value
    {
        @Override
        public Boolean getValue()
        {
            try {
                return value != null && (value instanceof String?Boolean.parseBoolean((String) value):(boolean) value);
            }catch (Exception e){e.printStackTrace();return false;}
        }
        @Override public Type getValueType(){return Type.BOOLEAN;}
    }
    public static class ValueInt extends Value implements INumber
    {
        @Override
        public Integer getValue()
        {
            try {
                return value instanceof String? Integer.parseInt((String)value): (value==null?0:(int)value);
            }catch (Exception e){e.printStackTrace();return 0;}
        }
        @Override public Type getValueType(){return Type.INT;}
    }
    public static class ValueFloat extends Value implements INumber
    {
        @Override
        public Float getValue()
        {
            try {
                return value instanceof String?(Float.parseFloat((String) value)):(value==null?0F:(float)value);
            }catch (Exception e){e.printStackTrace();return 0F;}
        }
        @Override public Type getValueType(){return Type.FLOAT;}
    }
    public static class ValueDouble extends Value implements INumber
    {
        @Override
        public Double getValue()
        {
            try {
                return value instanceof String?Double.parseDouble((String) value):(value==null?0D:(double)value);
            }catch (Exception e){e.printStackTrace();return 0d;}
        }
        @Override public Type getValueType(){return Type.DOUBLE;}
    }
    public static class ValueLong extends Value implements INumber
    {
        @Override
        public Long getValue()
        {
            try {
                return value instanceof String?Long.parseLong((String) value):value==null?0L:(long)value;
            }catch (Exception e){e.printStackTrace();return 0L;}
        }
        @Override public Type getValueType(){return Type.LONG;}
    }
    public static class ValueVector extends Value
    {
        @Override
        public Vector getValue()
        {
            try {
                return value instanceof Vector ? (Vector)value : null;
            }catch (Exception e){e.printStackTrace();return null;}
        }

        @Override
        public Value setValue(Object o)
        {
            if(o instanceof String){
                String[] sp = ((String) o).split(",");
                if(sp.length<3) sp = ((String) o).split(" ");
                if(sp.length<3) sp = ((String) o).split("/");
                if(sp.length<3) sp = ((String) o).split("-");
                if(sp.length<3) {value = new Vector();return this;}

                value = new Vector().setX(Double.parseDouble(sp[0])).setY(Double.parseDouble(sp[1])).setZ(Double.parseDouble(sp[2]));
            }
            return super.setValue(o);
        }

        @Override public Type getValueType(){return Type.VECTOR;}
    }

    public Options()
    {
    }
    public Options(Options<K> o)
    {
        for(K k : o.keySet())
            putDefault(k,o.get(k).getValue());
    }

    public static IValue create(Object o)
    {
        if(o instanceof String)         return Type.STRING  .create(o);
        else if(o instanceof Integer)   return Type.INT     .create(o);
        else if(o instanceof Float)     return Type.FLOAT   .create(o);
        else if(o instanceof Double)    return Type.DOUBLE  .create(o);
        else if(o instanceof Long)      return Type.LONG    .create(o);
        else if(o instanceof Boolean)   return Type.BOOLEAN .create(o);
        else if(o instanceof Vector)    return Type.VECTOR  .create(o);
        else                            return Type.OBJECT  .create(o);
    }

    public IValue putDefault(K key, Object value) {return super.put(key, create(value));}

    public Vector getVector(K key){return getVector(key,null);}
    public Vector getVector(K key,Vector def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof ValueVector)
            return ((ValueVector) v).getValue();
        return def;
    }

    public String getString(K key){return getString(key,null);}
    public String getString(K key,String def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof ValueString)
            return ((ValueString) v).getValue();
        else if(v instanceof ValueBoolean)
            return v.getValue().toString();
        return def;
    }
    public boolean getBoolean(K key){return getBoolean(key,false);}
    public boolean getBoolean(K key,boolean def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof ValueString)
            return Boolean.parseBoolean(((ValueString) v).getValue());
        else if(v instanceof ValueBoolean)
            return ((ValueBoolean)v).getValue();
        return def;
    }
    public int getInt(K key){return getInt(key,0);}
    public int getInt(K key,int def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof INumber)
            return (int)v.getValue();
        return def;
    }
    public long getLong(K key){return getLong(key,0L);}
    public long getLong(K key,long def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof INumber)
            return (long)v.getValue();
        return def;
    }
    public float getFloat(K key){return getFloat(key,0F);}
    public float getFloat(K key,float def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof INumber)
            return (float)v.getValue();
        return def;
    }
    public double getDouble(K key){return getDouble(key,0D);}
    public double getDouble(K key,double def)
    {
        IValue v = get(key);if(v==null)return def;
        if(v instanceof INumber)
            return (double)v.getValue();
        return def;
    }

}
