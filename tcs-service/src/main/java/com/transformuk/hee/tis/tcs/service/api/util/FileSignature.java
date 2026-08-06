package com.transformuk.hee.tis.tcs.service.api.util;

/**
 * Magic bytes used for file signature validation. Each value stores only the invariant prefix
 * bytes for that format;
 */
public enum FileSignature {
  PDF(new byte[]{ // .pdf
      (byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2D
  }),
  OLE2(new byte[]{ // .doc, .xls
      (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
      (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1
  }),
  ZIP(new byte[]{ // .docx, .xlsx
      (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04
  }),
  MZ(new byte[]{ // .exe
      (byte) 0x4D, (byte) 0x5A
  });

  private final byte[] bytes;

  FileSignature(final byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] bytes() {
    return bytes.clone();
  }
}
