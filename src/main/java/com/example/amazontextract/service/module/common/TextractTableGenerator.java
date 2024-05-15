package com.example.amazontextract.service.module.common;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.Relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TextractTableGenerator {

    public String[][] generateTableFromRelationship(List<Relationship> relationships, List<Block> cellBlocks, List<Block> lineBlocks) {
        String[][] tableMap = new String[0][0];
        if (relationships != null && !relationships.isEmpty()) {
            for (int x = 0; x < relationships.size(); x++) {
                Relationship relationship = relationships.get(x);
                if (relationship.type().toString().equals("CHILD")) {
                    List<Integer> rows = new ArrayList<>();
                    List<Integer> cols = new ArrayList<>();
                    relationship.ids().forEach(id -> {
                        Block cell = cellBlocks.stream().filter(b -> b.id().equals(id)).findFirst().orElse(null);
                        if (cell != null) {
                            rows.add(cell.rowIndex());
                            cols.add(cell.columnIndex());
                        }
                    });
                    tableMap = new String[Collections.max(rows)][Collections.max(cols)];
                    for (int y = 0; y < relationship.ids().size(); y++) {
                        String id = relationship.ids().get(y);
                        Block cell = cellBlocks.stream().filter(b -> b.id().equals(id)).findFirst().orElse(null);
                        if (cell != null) {
                            Integer row = cell.rowIndex();
                            Integer col = cell.columnIndex();
                            List<Relationship> relationshipsCell = cell.relationships();
                            if (relationshipsCell != null && !relationshipsCell.isEmpty()) {
                                // iterate over all new lines in the cell
                                int cellIndex = 0;
                                tableMap[row - 1][col - 1] = "";
                                do {
                                    String firstRelationship = relationshipsCell.get(0).ids().get(cellIndex);
                                    Block lineBlockRelated = lineBlocks.stream().filter(lineBlock -> lineBlock.relationships().get(0).ids().contains(firstRelationship)).findFirst().orElse(null);
                                    if (lineBlockRelated != null) {
                                        String space = cellIndex == 0 ? "" : " ";
                                        tableMap[row - 1][col - 1] += space + lineBlockRelated.text();
                                    } else {
                                        break;
                                    }
                                    cellIndex += lineBlockRelated.relationships().get(0).ids().size();
                                } while (cellIndex < relationshipsCell.get(0).ids().size());
                            }
                        }
                    }
                }
            }
        }
        return tableMap;
    }
}
