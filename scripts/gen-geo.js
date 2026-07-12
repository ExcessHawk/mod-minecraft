// Converts the Excalibur cuboid item model (java) into a GeckoLib Bedrock
// geometry (.geo.json) plus a small color-band texture, mapping each cube's
// UV to a solid color band by part name. No per-face painting needed.
const fs = require("fs"), zlib = require("zlib"), path = require("path");
const RES = path.resolve(__dirname, "../mythical-swords-template-1.20.1/src/main/resources/assets/mythicalswords");
const model = JSON.parse(fs.readFileSync(path.join(RES, "models/item/excalibur.json"), "utf8"));

// ---- color bands on a 16x16 texture (4x4 blocks), uv point inside each ----
const BAND = { gold:[1,1], blue:[5,1], silver:[9,1], cream:[13,1], light:[5,5] };
function colorOf(name){
  if (name === "gem_shine") return "light";
  if (name.startsWith("gem")) return "blue";
  if (name.startsWith("fuller")) return "cream";
  if (name === "grip_body") return "silver";
  return "gold";
}

// ---- build geo cubes (java -> bedrock coords; X mirrored) ----
const cubes = model.elements.map(e => {
  const [fx,fy,fz] = e.from, [tx,ty,tz] = e.to;
  const [u,v] = BAND[colorOf(e.name)];
  const faceUV = { uv:[u,v], uv_size:[1,1] };
  const cube = {
    origin: [ -tx, fy, fz ],
    size: [ +(tx-fx).toFixed(3), +(ty-fy).toFixed(3), +(tz-fz).toFixed(3) ],
    uv: { north:faceUV, east:faceUV, south:faceUV, west:faceUV, up:faceUV, down:faceUV }
  };
  if (e.rotation && e.rotation.axis === "z") {
    const o = e.rotation.origin;
    cube.pivot = [ -o[0], o[1], o[2] ];
    cube.rotation = [ 0, 0, e.rotation.angle ];
  }
  return cube;
});

const geo = {
  format_version: "1.12.0",
  "minecraft:geometry": [{
    description: {
      identifier: "geometry.excalibur",
      texture_width: 16, texture_height: 16,
      visible_bounds_width: 4, visible_bounds_height: 4,
      visible_bounds_offset: [0, 1.5, 0]
    },
    bones: [{ name: "excalibur", pivot: [0, 8, 0], cubes }]
  }]
};
fs.mkdirSync(path.join(RES, "geo"), { recursive: true });
fs.writeFileSync(path.join(RES, "geo/excalibur.geo.json"), JSON.stringify(geo, null, "\t"));

// ---- 16x16 banded texture ----
const COLORS = { gold:"#E6B92E", blue:"#2E6FE6", silver:"#D7D7CF", cream:"#F2E7BE", light:"#7FC8FF" };
const hex = h => [parseInt(h.slice(1,3),16),parseInt(h.slice(3,5),16),parseInt(h.slice(5,7),16),255];
const W=16,H=16, buf=new Uint8Array(W*H*4);
function block(px,py,rgba){ for(let y=py;y<py+4;y++)for(let x=px;x<px+4;x++){const o=(y*W+x)*4;buf[o]=rgba[0];buf[o+1]=rgba[1];buf[o+2]=rgba[2];buf[o+3]=rgba[3];} }
block(0,0,hex(COLORS.gold)); block(4,0,hex(COLORS.blue)); block(8,0,hex(COLORS.silver)); block(12,0,hex(COLORS.cream)); block(4,4,hex(COLORS.light));
// fill rest gold so no transparent gaps
for(let i=0;i<buf.length;i+=4){ if(buf[i+3]===0){const g=hex(COLORS.gold);buf[i]=g[0];buf[i+1]=g[1];buf[i+2]=g[2];buf[i+3]=255;} }

const CRC=(()=>{const t=new Uint32Array(256);for(let n=0;n<256;n++){let c=n;for(let k=0;k<8;k++)c=c&1?0xedb88320^(c>>>1):c>>>1;t[n]=c>>>0;}return t;})();
function crc32(b){let c=0xffffffff;for(let i=0;i<b.length;i++)c=CRC[(c^b[i])&0xff]^(c>>>8);return (c^0xffffffff)>>>0;}
function chunk(type,data){const len=Buffer.alloc(4);len.writeUInt32BE(data.length,0);const td=Buffer.concat([Buffer.from(type,"ascii"),data]);const crc=Buffer.alloc(4);crc.writeUInt32BE(crc32(td),0);return Buffer.concat([len,td,crc]);}
const raw=Buffer.alloc(H*(1+W*4)); for(let y=0;y<H;y++){raw[y*(1+W*4)]=0;for(let x=0;x<W*4;x++)raw[y*(1+W*4)+1+x]=buf[y*W*4+x];}
const png=Buffer.concat([Buffer.from([137,80,78,71,13,10,26,10]),chunk("IHDR",(()=>{const b=Buffer.alloc(13);b.writeUInt32BE(W,0);b.writeUInt32BE(H,4);b[8]=8;b[9]=6;return b;})()),chunk("IDAT",zlib.deflateSync(raw,{level:9})),chunk("IEND",Buffer.alloc(0))]);
fs.writeFileSync(path.join(RES, "textures/item/excalibur_geo.png"), png);

console.log("geo cubes:", cubes.length, "| wrote geo/excalibur.geo.json + textures/item/excalibur_geo.png");
