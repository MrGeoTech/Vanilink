# Protocol

Chunk Request:
  - Byte Packet Id:  0x01
  - Int (4 bytes) chunk x coordinate
  - Int (4 bytes) chunk z coordinate

Chunk Response:
  - Byte Packet Id: 0x02
  - Int (4 bytes) chunk data compressed length
  - Int (4 bytes) chunk data uncompressed length
  - ByteArray (varied, determined by previous int) chunk data

---------------

Chunk Format:
  - For each column:
    - Int (4 bytes) Column height
    - String Array (varied, determined by previous int) Column data
      - Integer: Name length (if it is 0, it means the current block is the same as the previous and '*' will be added to signify that)
      - NBTString: Name
    
NBT Format:
  - "amount" - Short: Amount of the same block in a row
  - "name" - String: NamespaceID of the blocks

TODO: Biomes
