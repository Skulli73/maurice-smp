package io.github.skulli73.mauriceSMP.skills.services;

import io.github.skulli73.mauriceSMP.MauriceSMP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public final class BlockMassDetectionService {
    private final MauriceSMP plugin;

    private final int MAX_TREE_SIZE;

    // Maps log types to their corresponding leaf types

    public BlockMassDetectionService() {
        this.plugin = MauriceSMP.getInstance();

        MAX_TREE_SIZE = plugin.getConfig().getInt("maxTreeSize", 1000);
    }
    public Set<Block> identifyTreeStructure(Block sourceBlock) {
        Material sourceType = sourceBlock.getType();
        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(sourceBlock);

        while (!queue.isEmpty() && visited.size() < MAX_TREE_SIZE) {
            Block current = queue.poll();

            if (visited.contains(current) || !isMatchingLog(current, sourceType))
                continue;

            visited.add(current);
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if (j == 0 && k == 0 && l == 0)
                            continue;
                        Block block = current.getRelative(j, k, l);
                        if (isMatchingLog(block, sourceType) && !visited.contains(block))
                            queue.add(block);
                    }
                }
            }
        }

        return visited;
    }


    private boolean isMatchingLog(Block block, Material logType) {
        return block.getType() == logType;
    }


}

