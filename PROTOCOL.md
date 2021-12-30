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

Chunk Format:  (Influenced by slime world chunk format & vanilla anvil format)
  - 16 bit chunk section bitmask
  - For each section:
    - Int (4 bytes) Palette length
    - NBT Array (varied, determined by previous int) Palette data
    - Int (4 bytes) Block states length
    - LongArray (varied, determined by previous int) Block states
  TODO: Biomes
