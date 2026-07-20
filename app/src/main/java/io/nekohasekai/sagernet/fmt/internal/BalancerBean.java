package io.nekohasekai.sagernet.fmt.internal;

import androidx.annotation.NonNull;

import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import java.util.ArrayList;
import java.util.List;

import io.nekohasekai.sagernet.fmt.KryoConverters;

public class BalancerBean extends InternalBean {

    public static final int TYPE_LIST = 0;
    public static final int TYPE_GROUP = 1;

    public Integer type;
    public String strategy;
    public List<Long> proxies;
    public Long groupId;

    public String probeUrl;
    public Integer probeInterval;
    public String nameFilter;
    public String nameFilter1;
    public Boolean useLandingProxy;
    public Boolean useFrontProxy;

    @Override
    public void initializeDefaultValues() {
        super.initializeDefaultValues();
        if (name == null) name = "";
        if (strategy == null) strategy = "";
        if (type == null) type = TYPE_LIST;
        if (proxies == null) proxies = new ArrayList<>();
        if (groupId == null) groupId = 0L;
        if (probeUrl == null) probeUrl = "";
        if (probeInterval == null) probeInterval = 300;
        if (nameFilter == null) nameFilter = "";
        if (nameFilter1 == null) nameFilter1 = "";
        if (useLandingProxy == null) useLandingProxy = false;
        if (useFrontProxy == null) useFrontProxy = false;
    }

    @Override
    public String displayName() {
        if (!name.isEmpty()) {
            return name;
        } else {
            return "Balancer " + Math.abs(hashCode());
        }
    }

    @Override
    public void serialize(ByteBufferOutput output) {
        output.writeInt(5);
        output.writeInt(type);
        output.writeString(strategy);
        switch (type) {
            case TYPE_LIST: {
                int length = proxies.size();
                output.writeInt(length);
                for (Long proxy : proxies) {
                    output.writeLong(proxy);
                }
                break;
            }
            case TYPE_GROUP: {
                output.writeLong(groupId);
                break;
            }
        }
        output.writeString(probeUrl);
        output.writeInt(probeInterval);
        if (type == TYPE_GROUP) {
            output.writeString(nameFilter);
            output.writeString(nameFilter1);
            output.writeBoolean(useLandingProxy);
            output.writeBoolean(useFrontProxy);
        }
    }

    @Override
    public void deserialize(ByteBufferInput input) {
        int version = input.readInt();
        type = input.readInt();
        strategy = input.readString();
        switch (type) {
            case TYPE_LIST: {
                int length = input.readInt();
                proxies = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    proxies.add(input.readLong());
                }
                break;
            }
            case TYPE_GROUP: {
                groupId = input.readLong();
                break;
            }
        }
        if (version >= 1) {
            probeUrl = input.readString();
            probeInterval = input.readInt();
        }
        if (version >= 2 && type == TYPE_GROUP) {
            nameFilter = input.readString();
        }
        if (version >= 3 && type == TYPE_GROUP) {
            nameFilter1 = input.readString();
        }
        if (version >= 4 && type == TYPE_GROUP) {
            useLandingProxy = input.readBoolean();
        }
        if (version >= 5 && type == TYPE_GROUP) {
            useFrontProxy = input.readBoolean();
        }
    }

    @NonNull
    @Override
    public BalancerBean clone() {
        return KryoConverters.deserialize(new BalancerBean(), KryoConverters.serialize(this));
    }

    public static final Creator<BalancerBean> CREATOR = new CREATOR<BalancerBean>() {
        @NonNull
        @Override
        public BalancerBean newInstance() {
            return new BalancerBean();
        }

        @Override
        public BalancerBean[] newArray(int size) {
            return new BalancerBean[size];
        }
    };

    public boolean isInsecure() {
        return false;
    }

}