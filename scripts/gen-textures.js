// Procedural 16x16 weapon texture generator (placeholder pixel art).
// Hand-rolled PNG encoder (no deps). Colors a simple sword by elemental affinity.
// Usage: node gen-textures.js
const fs = require("fs");
const zlib = require("zlib");
const path = require("path");

const OUT = path.resolve(
  __dirname,
  "../mythical-swords-template-1.20.1/src/main/resources/assets/mythicalswords/textures/item"
);

// ---- PNG encoder (16x16 RGBA, 8-bit) ----
const CRC_TABLE = (() => {
  const t = new Uint32Array(256);
  for (let n = 0; n < 256; n++) {
    let c = n;
    for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
    t[n] = c >>> 0;
  }
  return t;
})();
function crc32(buf) {
  let c = 0xffffffff;
  for (let i = 0; i < buf.length; i++) c = CRC_TABLE[(c ^ buf[i]) & 0xff] ^ (c >>> 8);
  return (c ^ 0xffffffff) >>> 0;
}
function chunk(type, data) {
  const len = Buffer.alloc(4);
  len.writeUInt32BE(data.length, 0);
  const td = Buffer.concat([Buffer.from(type, "ascii"), data]);
  const crc = Buffer.alloc(4);
  crc.writeUInt32BE(crc32(td), 0);
  return Buffer.concat([len, td, crc]);
}
function encodePNG(px, w, h) {
  // px: Uint8Array length w*h*4 (RGBA). Add filter byte 0 per row.
  const raw = Buffer.alloc(h * (1 + w * 4));
  for (let y = 0; y < h; y++) {
    raw[y * (1 + w * 4)] = 0;
    for (let x = 0; x < w * 4; x++) raw[y * (1 + w * 4) + 1 + x] = px[y * w * 4 + x];
  }
  const sig = Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]);
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(w, 0);
  ihdr.writeUInt32BE(h, 4);
  ihdr[8] = 8; // bit depth
  ihdr[9] = 6; // RGBA
  ihdr[10] = 0; ihdr[11] = 0; ihdr[12] = 0;
  const idat = zlib.deflateSync(raw, { level: 9 });
  return Buffer.concat([
    sig,
    chunk("IHDR", ihdr),
    chunk("IDAT", idat),
    chunk("IEND", Buffer.alloc(0)),
  ]);
}

// ---- drawing ----
const N = 16;
function newCanvas() {
  return new Uint8Array(N * N * 4); // transparent
}
function set(px, x, y, c) {
  if (x < 0 || y < 0 || x >= N || y >= N) return;
  const i = (y * N + x) * 4;
  px[i] = c[0]; px[i + 1] = c[1]; px[i + 2] = c[2]; px[i + 3] = c.length > 3 ? c[3] : 255;
}

// Affinity palettes: [edge(light), core(main), glow]
const PAL = {
  FIRE:      [[255, 196, 120], [240, 90, 30],  [255, 230, 150]],
  ICE:       [[200, 245, 255], [70, 190, 230], [220, 250, 255]],
  LIGHTNING: [[255, 248, 170], [250, 205, 50], [255, 255, 210]],
  DIVINE:    [[255, 246, 210], [235, 200, 90], [255, 255, 235]],
  DARK:      [[190, 140, 220], [110, 55, 160], [205, 170, 235]],
  NATURE:    [[180, 235, 160], [70, 175, 70],  [205, 245, 190]],
};
const OUTLINE = [25, 22, 30];
const GUARD = [90, 90, 100];
const GUARD_HI = [140, 140, 150];
const HANDLE = [95, 60, 30];
const HANDLE_HI = [130, 90, 50];
const POMMEL = [205, 170, 60];

// Vertical sword centered on cols 7-8. Tip top, pommel bottom.
function drawSword(affinity) {
  const px = newCanvas();
  const p = PAL[affinity] || PAL.DIVINE;
  const [edge, core, glow] = p;

  // outline column borders (col 6 and 9) along blade for definition
  for (let y = 1; y <= 10; y++) {
    set(px, 6, y, OUTLINE);
    set(px, 9, y, OUTLINE);
  }
  // tip
  set(px, 7, 0, OUTLINE);
  set(px, 8, 0, OUTLINE);
  set(px, 7, 1, glow);
  set(px, 8, 1, edge);
  // blade body rows 2..9
  for (let y = 2; y <= 9; y++) {
    set(px, 7, y, edge); // light edge
    set(px, 8, y, core); // core
  }
  // bottom of blade meets guard
  set(px, 7, 10, edge);
  set(px, 8, 10, core);

  // crossguard row 11, cols 4..11
  for (let x = 4; x <= 11; x++) set(px, x, 11, GUARD);
  set(px, 4, 11, OUTLINE);
  set(px, 11, 11, OUTLINE);
  set(px, 5, 11, GUARD_HI);
  set(px, 10, 11, GUARD_HI);

  // handle rows 12..14 cols 7-8
  for (let y = 12; y <= 14; y++) {
    set(px, 7, y, HANDLE_HI);
    set(px, 8, y, HANDLE);
  }
  // pommel row 15
  set(px, 7, 15, POMMEL);
  set(px, 8, 15, POMMEL);
  set(px, 6, 15, OUTLINE);
  set(px, 9, 15, OUTLINE);

  // glow sparkles near tip
  set(px, 9, 2, glow);
  set(px, 6, 4, glow);

  return px;
}

// Staff/scepter variant (Was Scepter, Ruyi) - longer shaft, orb head
function drawStaff(affinity) {
  const px = newCanvas();
  const p = PAL[affinity] || PAL.DIVINE;
  const [edge, core, glow] = p;
  // shaft cols 7-8 rows 3..15
  for (let y = 3; y <= 15; y++) {
    set(px, 7, y, HANDLE_HI);
    set(px, 8, y, HANDLE);
  }
  // orb/head rows 0..3 (diamond)
  const orb = [
    [8, 0], [7, 1], [8, 1], [9, 1], [6, 2], [7, 2], [8, 2], [9, 2], [10, 2], [7, 3], [8, 3], [9, 3],
  ];
  for (const [x, y] of orb) set(px, x, y, core);
  set(px, 8, 0, glow);
  set(px, 8, 2, edge);
  set(px, 7, 2, glow);
  // outline around orb
  set(px, 8, -1, OUTLINE);
  set(px, 5, 2, OUTLINE);
  set(px, 11, 2, OUTLINE);
  return px;
}

// Targets: name -> {affinity, shape}
const TARGETS = {
  excalibur:        { affinity: "DIVINE", shape: "sword" },
  caliburn:         { affinity: "DIVINE", shape: "sword" },
  jian:             { affinity: "LIGHTNING", shape: "sword" },
  ruyi_jingu_bang:  { affinity: "NATURE", shape: "staff" },
};

let count = 0;
for (const [name, cfg] of Object.entries(TARGETS)) {
  const px = cfg.shape === "staff" ? drawStaff(cfg.affinity) : drawSword(cfg.affinity);
  const png = encodePNG(px, N, N);
  fs.writeFileSync(path.join(OUT, name + ".png"), png);
  console.log(`wrote ${name}.png  ${cfg.affinity}/${cfg.shape}  ${png.length}b`);
  count++;
}
console.log(`done: ${count} textures`);
