package net.shirojr.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CodecUtils {
    /**
     * Needed for {@link Codec#unboundedMap(Codec, Codec) Codec.unboundedMap} since it requires String keys
     *
     */
    public static final Codec<BlockPos> BLOCKPOS_STRING_CODEC = Codec.STRING.comapFlatMap(
            string -> {
                String[] components = string.split(",");
                if (components.length != 3) {
                    return DataResult.error(() -> "3 components required");
                }
                return parseInt(components[0])
                        .flatMap(x -> parseInt(components[1])
                                .flatMap(y -> parseInt(components[2])
                                        .map(z -> new BlockPos(x, y, z)
                                        )
                                )
                        );
            },
            pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
    );

    public static <K, V> Codec<HashMap<K, V>> hashMapCodec(Codec<K> keyCodec, Codec<V> valueCodec) {
        return Codec.pair(keyCodec, valueCodec).listOf().xmap(
                list -> list.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (a, b) -> a, HashMap::new)),
                map -> new ArrayList<>(map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).toList())
        );
    }


    // --------------------------------------------- internal ---------------------------------------------

    private static DataResult<Integer> parseInt(String s) {
        try {
            return DataResult.success(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return DataResult.error(() -> "Not an integer: " + s);
        }
    }
}
