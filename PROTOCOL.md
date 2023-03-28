# Protocol
## Open Connection
### Request
- `0x01` - Request ID - 1 Byte
- `secretLength` - Secret Length - 4 Bytes
- `secret` - Secret - `secretLength` Bytes
### Response
- `0x01` - Response ID - 1 Byte
- `success` - Success - 1 Byte
## Close Connection
### Request
- `0xFF` - Request ID - 1 Byte
The both the server and the client will close the connection after receiving this request.
## Chunk Request
### Request
- `0x02` - Request ID - 1 Byte
- `startX` - Start X - 4 Bytes
- `startY` - Start Y - 4 Bytes
- `startZ` - Start Z - 4 Bytes
- `sizeX` - Size X - 4 Bytes
- `sizeY` - Size Y - 4 Bytes
- `sizeZ` - Size Z - 4 Bytes
### Response
- `0x02` - Response ID - 1 Byte
- `compressedLength` - Compressed Length - 4 Bytes
- `uncompressedLength` - Uncompressed Length - 4 Bytes
- `compressedData` - Compressed Data - `compressedLength` Bytes

## Chunk Response Data Format
- Block Data - `namespace[properties]`
- Block separator - `|`

## Chunk Response Data Example
```
minecraft:air|minecraft:air|minecraft:oak_log[axis=x]...minecraft:air|minecraft:air;
```