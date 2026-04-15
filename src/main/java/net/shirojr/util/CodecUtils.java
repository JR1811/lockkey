package net.shirojr.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;

public class CodecUtils {
    /**
     * Needed for {@link Codec#unboundedMap(Codec, Codec) Codec.unboundedMap} since it requires String keys
     *
     * @see <a href="https://github.com/Fusion-Flux/Portal-Cubed-Rewrite/blob/b3f58665d144c16e8997f69d0c250cb27e5aedcc/src/main/java/io/github/fusionflux/portalcubed/framework/util/PortalCubedCodecs.java#L31-L44">Original</a>
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


    // --------------------------------------------- internal ---------------------------------------------

    private static DataResult<Integer> parseInt(String s) {
        try {
            return DataResult.success(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return DataResult.error(() -> "Not an integer: " + s);
        }
    }
}
