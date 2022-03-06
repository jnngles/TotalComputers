/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

#ifndef TC_AWT_H
#define TC_AWT_H

#include "object.h"
#include "util.h"
#include "jmath.h"
#include "text.h"
#include "io.h"

/// STRUCT

namespace awt {
    namespace color {
        class ColorSpace;
    }

    namespace font {
        class FontRenderContext;
        class GlyphJustificationInfo;
        class GlyphMetrics;
        class GlyphVector;
        class LineMetrics;
    }

    namespace geom {
        class PathIterator;
        class AffineTransform;
        class Dimension2D;
        class Line2D;
        class Point2D;
        class Rectangle2D;
        class RectangularShape;
    }

    namespace image {
        namespace renderable {
            class RenderableImage;
            class RenderContext;
        }

        class BufferedImageOp;
        class ImageObserver;
        class ImageConsumer;
        class ImageProducer;
        class RenderedImage;
        class TileObserver;
        class WritableRenderedImage;
        class BufferedImage;
        class ColorModel;
        class IndexColorModel;
        class DataBuffer;
        class Raster;
        class SampleModel;
        class VolatileImage;
        class WritableRaster;
    }

    class Composite;
    class CompositeContext;
    class Paint;
    class PaintContext;
    class Shape;
    class Stroke;
    class Transparency;
    class BufferCapabilities;
    class Color;
    class Dimension;
    class Font;
    class FontMetrics;
    class Graphics;
    class Graphics2D;
    class GraphicsConfiguration;
    class Image;
    class ImageCapabilities;
    class Point;
    class Polygon;
    class Rectangle;
    class RenderingHints;
}

/// CLASSES

namespace awt {

namespace geom {

class Dimension2D : public lang::Object {
public:
    Dimension2D(jobject instance);
    ~Dimension2D();

public:
    lang::Object Clone();
    double GetHeight();
    double GetWidth();
    void SetSize(Dimension2D d);
    void SetSize(double width, double height);

public:
    jobject instance;

};

}

class Dimension : public geom::Dimension2D {
public:
    Dimension(jobject instance);
    ~Dimension();

public:
    Dimension();
    Dimension(Dimension& d);
    Dimension(int width, int height);
    
public:
    bool Equals(lang::Object obj);
    double GetHeight();
    Dimension GetSize();
    double GetWidth();
    int HashCode();
    void SetSize(Dimension d);
    void SetSize(double width, double height);
    void SetSize(int width, int height);
    char* ToString();

public:
    jobject instance;

};

namespace geom {

class AffineTransform : public lang::Object {
public:
    AffineTransform(jobject instance);
    ~AffineTransform();

public:
    AffineTransform();
    AffineTransform(AffineTransform& Tx);
    AffineTransform(double* flatmatrix, int flatmatrixc);
    AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12);
    AffineTransform(float* flatmatrix, int flatmatrixc);
    AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12);

public:
    const static int TYPE_FLIP = 64;
    const static int TYPE_GENERAL_ROTATION = 16;
    const static int TYPE_GENERAL_SCALE = 4;
    const static int TYPE_GENERAL_TRANSFORM = 32;
    const static int TYPE_IDENTITY = 0;
    const static int TYPE_MASK_ROTATION = 24;
    const static int TYPE_MASK_SCALE = 6;
    const static int TYPE_QUADRANT_ROTATION = 8;
    const static int TYPE_TRANSLATION = 1;
    const static int TYPE_UNIFORM_SCALE = 2;

public:
    lang::Object Clone();
    void Concatenate(AffineTransform Tx);
    AffineTransform CreateInverse();
    Shape CreateTransformedShape(Shape pSrc);
    void DeltaTransform(double* srcPts, int srcPtsc, int stcOff, double* dstPts, int dstPtsc, int dstOff, int numPts);
    Point2D DeltaTransform(Point2D ptSrc, Point2D ptDst);
    bool Equals(lang::Object obj);
    double GetDeterminant();
    void GetMatrix(double* flatmatrix, int flatmatrixc);
    static AffineTransform GetQuadrantRotateInstance(int numquadrants);
    static AffineTransform GetQuadrantRotateInstance(int numquadrants, double anchorx, double anchory);
    static AffineTransform GetRotateInstance(double theta);
    static AffineTransform GetRotateInstance(double vecx, double vecy);
    static AffineTransform GetRotateInstance(double theta, double anchorx, double anchory);
    static AffineTransform GetScaleInstance(double sx, double sy);
    double GetScaleX();
    double GetScaleY();
    static AffineTransform GetShearInstance(double sdx, double shy);
    double GetShearX();
    double GetShearY();
    static AffineTransform GetTranslateInstance(double tx, double ty);
    double GetTranslateX();
    double GetTranslateY();
    int GetType();
    int HashCode();
    void InverseTransform(double* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts);
    Point2D InverseTransform(Point2D ptSrc, Point2D ptDst);
    void Invert();
    bool IsIdentity();
    void PreConcatenate(AffineTransform Tx);
    void QuadrantRotate(int numquadrants);
    void QuadrantRotate(int numquadrants, double anchorx, double anchory);
    void Rotate(double theta);
    void Rotate(double vecx, double vecy);
    void Rotate(double theta, double anchorx, double anchory);
    void Rotate(double vecx, double vecy, double anchorx, double anchory);
    void Scale(double sx, double sy);
    void SetToIdentity();
    void SetToQuadrantRotation(int numquadrants);
    void SetToQuadrantRotation(int numquadrants, double anchorx, double anchory);
    void SetToRotation(double theta);
    void SetToRotation(double vecx, double vecy);
    void SetToRotation(double theta, double anchorx, double anchory);
    void SetToRotation(double vecx, double vecy, double anchorx, double anchory);
    void SetToScale(double sx, double sy);
    void SetToShear(double sdx, double shy);
    void SetToTranslation(double tx, double ty);
    void SetTransform(AffineTransform Tx);
    void SetTransform(double m00, double m10, double m01, double m11, double m02, double m12);
    void Shear(double shx, double shy);
    char* ToString();
    void Transform(double* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts);
    void Transform(double* srcPts, int srcPtsc, int srcOff, float* dstPts, int dstPtsc, int dstOff, int numPts);
    void Transform(float* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts);
    void Transform(float* srcPts, int srcPtsc, int srcOff, float* dstPts, int dstPtsc, int dstOff, int numPts);
    void Transform(Point2D* ptSrc, int ptSrcc, int srcOff, Point2D* ptDst, int ptDstc, int dstOff, int numPts);
    Point2D Transform(Point2D ptSrc, Point2D ptDst);
    void Translate(double tx, double ty);

public:
    jobject instance;

};

class PathIterator : public NativeObjectInstance {
public:
    PathIterator(jobject instance);
    ~PathIterator();

public:
    const static int SEG_CLOSE = 4;
    const static int SEG_CUBICTO = 3;
    const static int SEG_LINETO = 1;
    const static int SEG_MOVETO = 0;
    const static int SEG_QUADTO = 2;
    const static int WIND_EVEN_ODD = 0;
    const static int WIND_NON_ZERO = 1;

public:
    int CurrentSegment(double* coords, int coordsc);
    int CurrentSegment(float* coords, int coordsc);
    int GetWindingRule();
    bool IsDone();
    void Next();

public:
    jobject instance;

};

}

class Shape : public NativeObjectInstance {
public:
    Shape(jobject instance);
    ~Shape();

public:
    bool Contains(double x, double y);
    bool Contains(double x, double y, double w, double h);
    bool Contains(geom::Point2D p);
    bool Contains(geom::Rectangle2D r);
    Rectangle GetBounds();
    geom::Rectangle2D GetBounds2D();
    geom::PathIterator GetPathIterator(geom::AffineTransform at);
    geom::PathIterator GetPathIterator(geom::AffineTransform at, double flatness);
    bool Intersects(double x, double y, double w, double h);
    bool Intersects(geom::Rectangle2D r);

public:
    jobject instance;

};

namespace geom {

class Line2D : public lang::Object, public Shape {
public:
    class Double;
    class Float;

public:
    Line2D(jobject instance);
    ~Line2D();

public:
    lang::Object Clone();
    bool Contains(double x, double y);
    bool Contains(double x, double y, double w, double h);
    bool Contains(Point2D p);
    bool Contains(Rectangle2D r);
    Rectangle GetBounds();
    Point2D GetP1();
    Point2D GetP2();
    PathIterator GetPathIterator(AffineTransform at);
    PathIterator GetPathIterator(AffineTransform at, double flatness);
    double GetX1();
    double GetX2();
    double GetY1();
    double GetY2();
    bool Intersects(double x, double y, double w, double h);
    bool Intersects(Rectangle2D r);
    bool IntersectsLine(double x1, double y1, double x2, double y2);
    bool IntersectsLine(Line2D l);
    static bool LinesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4);
    double PtLineDist(double px, double py);
    double PtLineDist(Point2D pt);
    double PtLineDistSq(double px, double py);
    static double PtLineDistSq(double x1, double y1, double x2, double y2, double px, double py);
    double ptLineDistSq(Point2D pt);
    double PtSegDist(double px, double py);
    static double PtSegDist(double x1, double y1, double x2, double y2, double px, double py);
    double PtSegDist(Point2D pt);
    double ptSegDistSq(double px, double py);
    static double PtSegDistSq(double x1, double y1, double x2, double y2, double px, double py);
    double PtSegDistSq(Point2D pt);
    int RelativeCCW(double px, double py);
    static int RelativeCCW(double x1, double y1, double x2, double y2, double px, double py);
    int RelativeCCW(Point2D p);
    void SetLine(double x1, double y1, double x2, double y2);
    void SetLine(Line2D l);
    void SetLine(Point2D p1, Point2D p2);

public:
    jobject instance;

};

class Line2D::Double : public Line2D {
public:
    Double(jobject instance);
    ~Double();

public:
    Double();
    Double(double x1, double y1, double x2, double y2);
    Double(Point2D p1, Point2D p2);

public:
    Rectangle2D GetBounds2D();
    Point2D GetP1();
    Point2D GetP2();
    double GetX1();
    double GetX2();
    double GetY1();
    double GetY2();
    void SetLine(double x1, double y1, double x2, double y2);

public:
    jobject instance;

};

class Line2D::Float : public Line2D {
public:
    Float(jobject instance);
    ~Float();

public:
    Float();
    Float(float x1, float y1, float x2, float y2);
    Float(Point2D p1, Point2D p2);

public:
    Rectangle2D GetBounds2D();
    Point2D GetP1();
    Point2D GetP2();
    double GetX1();
    double GetX2();
    double GetY1();
    double GetY2();
    void SetLine(double x1, double y1, double x2, double y2);
    void SetLine(float x1, float y1, float x2, float y2);

public:
    jobject instance;

};

class RectangularShape : public lang::Object, public Shape {
public:
    RectangularShape(jobject instance);
    ~RectangularShape();

public:
    lang::Object Clone();
    bool Contains(Point2D p);
    bool Contains(Rectangle2D r);
    Rectangle GetBounds();
    double GetCenterX();
    double GetCenterY();
    Rectangle2D GetFrame();
    double GetHeight();
    double GetMaxX();
    double GetMaxY();
    double GetMinX();
    double GetMinY();
    PathIterator GetPathIterator(AffineTransform at, double flatness);
    double GetWidth();
    double GetX();
    double GetY();
    bool Intersects(Rectangle2D r);
    bool IsEmpty();
    void SetFrame(double x, double y, double w, double h);
    void SetFrame(Point2D loc, Dimension2D size);
    void SetFrame(Rectangle2D r);
    void SetFrameFromCenter(double centerX, double centerY, double cornerX, double cronerY);
    void SetFrameFromCenter(Point2D center, Point2D corner);
    void SetFrameFromDiagonal(double x1, double y1, double x2, double y2);
    void SetFrameFromDiagonal(Point2D p1, Point2D p2);

public:
    jobject instance;

};

class Rectangle2D : public RectangularShape {
public:
    class Double;
    class Float;

public:
    Rectangle2D(jobject instance);
    ~Rectangle2D();

public:
    const static int OUT_BOTTOM = 8;
    const static int OUT_LEFT = 1;
    const static int OUT_RIGHT = 4;
    const static int OUT_TOP = 2;

public:
    void Add(double newx, double newy);
    void Add(Point2D pt);
    void Add(Rectangle2D r);
    bool Contains(double x, double y);
    bool Contains(double x, double y, double w, double h);
    Rectangle2D CreateIntersection(Rectangle2D r);
    Rectangle2D CreateUnion(Rectangle2D r);
    bool Equals(lang::Object obj);
    Rectangle2D GetBounds2D();
    PathIterator GetPathIterator(AffineTransform at);
    PathIterator GetPathIterator(AffineTransform at, double flatness);
    int HashCode();
    static void Intersect(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest);
    bool Intersects(double x, double y, double w, double h);
    bool IntersectsLine(double x1, double y1, double x2, double y2);
    bool IntersectsLine(Line2D l);
    int Outcode(double x, double y);
    int Outcode(Point2D p);
    void SetFrame(double x, double y, double w, double h);
    void SetRect(double x, double y, double w, double h);
    void SetRect(Rectangle2D r);
    void Union(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest);

public:
    jobject instance;

};

class Rectangle2D::Double : public Rectangle2D {
public:
    Double(jobject instance);
    ~Double();

public:
    Double();
    Double(double x, double y, double w, double h);

public:
    Rectangle2D CreateIntersection(Rectangle2D r);
    Rectangle2D CreateUnion(Rectangle2D r);
    Rectangle2D GetBounds2D();
    double GetHeight();
    double GetWidth();
    double GetX();
    double GetY();
    bool IsEmpty();
    int Outcode(double x, double y);
    void SetRect(double x, double y, double w, double h);
    void SetRect(Rectangle2D r);
    char* ToString();

public:
    jobject instance;

};

class Rectangle2D::Float : public Rectangle2D {
public:
    Float(jobject instance);
    ~Float();

public:
    Float();
    Float(float x, float y, float w, float h);

public:
    Rectangle2D CreateIntersection(Rectangle2D r);
    Rectangle2D CreateUnion(Rectangle2D r);
    Rectangle2D GetBounds2D();
    double GetHeight();
    double GetWidth();
    double GetX();
    double GetY();
    bool IsEmpty();
    int Outcode(double x, double y);
    void SetRect(double x, double y, double w, double h);
    void SetRect(float x, float y, float w, float h);
    void SetRect(Rectangle2D r);
    char* ToString();

public:
    jobject instance;

};

}

class Rectangle : public geom::Rectangle2D {
public:
    Rectangle(jobject instance);
    ~Rectangle();

public:
    Rectangle();
    Rectangle(Dimension d);
    Rectangle(int width, int height);
    Rectangle(int x, int y, int width, int height);
    Rectangle(Point p);
    Rectangle(Point p, Dimension d);
    Rectangle(Rectangle& r);

public:
    void Add(int newx, int newy);
    void Add(Point pt);
    void Add(Rectangle r);
    bool Contains(int x, int y);
    bool Contains(int X, int Y, int W, int H);
    bool Contains(Point p);
    bool Contains(Rectangle r);
    geom::Rectangle2D CreateIntersection(geom::Rectangle2D r);
    geom::Rectangle2D CreateUnion(geom::Rectangle2D r);
    bool Equals(lang::Object obj);
    Rectangle GetBounds();
    geom::Rectangle2D GetBounds2D();
    double GetHeight();
    Point GetLocation();
    Dimension GetSize();
    double GetWidth();
    double GetX();
    double GetY();
    void Grow(int h, int v);
    Rectangle Intersection(Rectangle r);
    bool Intersects(Rectangle r);
    bool IsEmpty();
    int Outcode(double x, double y);
    void SetBounds(int x, int y, int width, int height);
    void SetBounds(Rectangle r);
    void SetLocation(int x, int y);
    void SetLocation(Point p);
    void SetRect(double x, double y, double width, double height);
    void SetSize(Dimension d);
    void SetSize(int width, int height);
    char* ToString();
    void Translate(int dx, int dy);
    Rectangle Union(Rectangle r);

public:
    jobject instance;

};

namespace geom {

class Point2D : public lang::Object {
public:
    class Double;
    class Float;

public:
    Point2D(jobject instance);
    ~Point2D();

protected:
    Point2D();

public:
    lang::Object Clone();
    double Distance(double px, double py);
    static double Distance(double x1, double y1, double x2, double y2);
    double Distance(Point2D pt);
    double DistanceSq(double px, double py);
    static double DistanceSq(double x1, double y1, double x2, double y2);
    double DistanceSq(Point2D pt);
    bool Equals(lang::Object obj);
    double GetX();
    double GetY();
    int HashCode();
    void SetLocation(double x, double y);
    void SetLocation(Point2D p);

public:
    jobject instance;

};

class Point2D::Double : public Point2D {
public:
    Double(jobject instance);
    ~Double();

public:
    Double();
    Double(double x, double y);

public:
    double GetX();
    double GetY();
    void SetLocation(double x, double y);
    char* ToString();

public:
    jobject instance;

};

class Point2D::Float : public Point2D {
public:
    Float(jobject instance);
    ~Float();

public:
    Float();
    Float(float x, float y);

public:
    double GetX();
    double GetY();
    void SetLocation(double x, double y);
    void SetLocation(float x, float y);
    char* ToString();

public:
    jobject instance;

};

}

class Point : public geom::Point2D {
public:
    Point(jobject instance);
    ~Point();

public:
    Point();
    Point(int x, int y);
    Point(Point& p);

public:
    bool Equals(lang::Object obj);
    Point GetLocation();
    double GetX();
    double GetY();
    void Move(int x, int y);
    void SetLocation(double x, double y);
    void SetLocation(int x, int y);
    void SetLocation(Point p);
    char* ToString();
    void Translate(int dx, int dy);

public:
    jobject instance;

};

class Polygon : public lang::Object, public Shape {
public:
    Polygon(jobject instance);
    ~Polygon();

public:
    Polygon();
    Polygon(int* xpoints, int xpointsc, int* ypoints, int ypointsc, int npoints);

public:
    void AddPoint(int x, int y);
    bool Contains(double x, double y);
    bool Contains(double x, double y, double w, double h);
    bool Contains(int x, int y);
    bool Contains(Point p);
    bool Contains(geom::Point2D p);
    bool Contains(geom::Rectangle2D r);
    Rectangle GetBounds();
    geom::Rectangle2D GetBounds2D();
    geom::PathIterator GetPathIterator(geom::AffineTransform at);
    geom::PathIterator GetPathIterator(geom::AffineTransform at, double flatness);
    bool Intersects(double x, double y, double w, double h);
    bool Intersects(geom::Rectangle2D r);
    void Invalidate();
    void Reset();
    void Translate(int deltaX, int deltaY);

public:
    jobject instance;

};

class Transparency : public NativeObjectInstance {
public:
    Transparency(jobject instance);
    ~Transparency();

public:
    const static int BITMASK = 2;
    const static int OPAQUE = 1;
    const static int TRANSLUCENT = 3;

public:
    int GetTransparency();

public:
    jobject instance;

};

namespace color {

class ColorSpace : public lang::Object {
public:
    ColorSpace(jobject instance);
    ~ColorSpace();

public:
    const static int CS_CIEXYZ = 1001;
    const static int CS_GRAY = 1003;
    const static int CS_LINEAR_RGB = 1004;
    const static int CS_PYCC = 1002;
    const static int CS_sRGB = 1000;
    const static int TYPE_2CLR = 12;
    const static int TYPE_3CLR = 13;
    const static int TYPE_4CLR = 14;
    const static int TYPE_5CLR = 15;
    const static int TYPE_6CLR = 16;
    const static int TYPE_7CLR = 17;
    const static int TYPE_8CLR = 18;
    const static int TYPE_9CLR = 19;
    const static int TYPE_ACLR = 20;
    const static int TYPE_BCLR = 21;
    const static int TYPE_CCLR = 22;
    const static int TYPE_CMY = 11;
    const static int TYPE_CMYK = 9;
    const static int TYPE_DCLR = 23;
    const static int TYPE_ECLR = 24;
    const static int TYPE_FCLR = 25;
    const static int TYPE_GRAY = 6;
    const static int TYPE_HLS = 8;
    const static int TYPE_HSV = 7;
    const static int TYPE_Lab = 1;
    const static int TYPE_Luv = 2;
    const static int TYPE_RGB = 5;
    const static int TYPE_XYZ = 0;
    const static int TYPE_YCbCr = 3;
    const static int TYPE_Yxy = 4;

public:
    float* FromCIEXYZ(int* size, float* colorvalue, int colorvaluec);
    float* FromRGB(int* size, float* rgbvalue, int rgbvaluec);
    static ColorSpace GetInstance(int colorspace);
    float GetMaxValue(int component);
    float GetMinValue(int component);
    char* GetName(int idx);
    int GetNumComponents();
    int GetType();
    bool isCS_sRGB();
    float* ToCIEXYZ(int* size, float* colorvalue, int colorvaluec);
    float* ToRGB(int* size, float* colorvalue, int colorvaluec);

public:
    jobject instance;

};

}

namespace image {

namespace renderable {

class RenderContext : public lang::Object {
public:
    RenderContext(jobject instance);
    ~RenderContext();

public:
    RenderContext(geom::AffineTransform usr2dev);
    RenderContext(geom::AffineTransform usr2dev, RenderingHints hints);
    RenderContext(geom::AffineTransform usr2dev, Shape aoi);
    RenderContext(geom::AffineTransform usr2dev, Shape aoi, RenderingHints hints);

public:
    lang::Object Clone();
    void ConcatenateTransform(geom::AffineTransform modTransform);
    Shape GetAreaOfInterest();
    RenderingHints GetRenderingHints();
    geom::AffineTransform GetTransform();
    void PreConcatenateTransform(geom::AffineTransform modTransform);
    void SetAreaOfInterest(Shape newAoi);
    void SetRenderingHints(RenderingHints hints);
    void SetTransform(geom::AffineTransform newTransform);

public:
    jobject instance;

};

class RenderableImage : public NativeObjectInstance {
public:
    RenderableImage(jobject instance);
    ~RenderableImage();
    
public:
    static constexpr const char* const HINTS_OBSERVED = "HINTS_OBSERVED";
    
public:
    RenderedImage CreateDefaultRendering();
    RenderedImage CreateRendering(RenderContext renderContext);
    RenderedImage CreateScaledRendering(int w, int h, RenderingHints hints);
    float GetHeight();
    float GetMinX();
    float GetMinY();
    lang::Object GetProperty(const char* name);
    char** GetPropertyNames(int* size);
    util::Vector GetSources();
    float GetWidth();
    bool IsDynamic();

public:
    jobject instance;

};

}

class DataBuffer : public lang::Object {
public:
    DataBuffer(jobject instance);
    ~DataBuffer();

public:
    const static int TYPE_BYTE = 0;
    const static int TYPE_DOUBLE = 5;
    const static int TYPE_FLOAT = 4;
    const static int TYPE_INT = 3;
    const static int TYPE_SHORT = 2;
    const static int TYPE_UNDEFINED = 32;
    const static int TYPE_USHORT = 1;
    
protected:
    DataBuffer(int dataType, int size);
    DataBuffer(int dataType, int size, int numBanks);
    DataBuffer(int dataType, int size, int numBanks, int offset);
    DataBuffer(int dataType, int size, int numBanks, int* offsets, int offsetsc);

public:
    int GetDataType();
    static int GetDataTypeSize(int type);
    int GetElem(int i);
    int GetElem(int bank, int i);
    double GetElemDouble(int i);
    double GetElemDouble(int bank, int i);
    float GetElemFloat(int i);
    float GetElemFloat(int bank, int i);
    int GetNumBanks();
    int GetOffset();
    int* GetOffsets(int* size);
    int GetSize();
    void SetElem(int i, int val);
    void SetElem(int bank, int i, int val);
    void SetElemDouble(int i, double val);
    void SetElemDouble(int bank, int i, double val);
    void SetElemFloat(int i, float val);
    void SetElemFloat(int bank, int i, float val);

public:
    jobject instance;

};

class SampleModel : public lang::Object {
public:
    SampleModel(jobject instance);
    ~SampleModel();

public:
    SampleModel CreateCompatibleSampleModel(int w, int h);
    DataBuffer CreateDataBuffer();
    SampleModel CreateSubsetSampleModel(int* bands, int bandsc);
    lang::Object GetDataElements(int x, int y, int w, int h, lang::Object obj, DataBuffer data);
    lang::Object GetDataElements(int x, int y, lang::Object obj, DataBuffer data);
    int GetDataType();
    int GetHeight();
    int GetNumBands();
    int GetNumDataElements();
    double* GetPixel(int* size, int x, int y, double* dArray, int dArrayc, DataBuffer data);
    float* GetPixel(int* size, int x, int y, float* fArray, int fArrayc, DataBuffer data);
    int* GetPixel(int* size, int x, int y, int* iArray, int iArrayc, DataBuffer data);
    double* GetPixels(int* size, int x, int y, int w, int h, double* dArray, int dArrayc, DataBuffer data);
    float* GetPixels(int* size, int x, int y, int w, int h, float* fArray, int fArrayc, DataBuffer data);
    int* GetPixels(int* size, int x, int y, int w, int h, int* iArray, int iArrayc, DataBuffer data);
    int GetSample(int x, int y, int b, DataBuffer data);
    double GetSampleDouble(int x, int y, int b, DataBuffer data);
    float GetSampleFloat(int x, int y, int b, DataBuffer data);
    double* GetSamples(int* size, int x, int y, int w, int h, int b, double* dArray, int dArrayc, DataBuffer data);
    float* GetSamples(int* size, int x, int y, int w, int h, int b, float* fArray, int fArrayc, DataBuffer data);
    int* GetSamples(int* size, int x, int y, int w, int h, int b, int* iArray, int iArrayc, DataBuffer data);
    int* GetSampleSize(int* size);
    int GetSampleSize(int band);
    int GetTransferType();
    int GetWidth();
    void SetDataElements(int x, int y, int w, int h, lang::Object obj, DataBuffer data);
    void SetDataElements(int x, int y, lang::Object obj, DataBuffer data);
    void SetPixel(int x, int y, double* dArray, int dArrayc, DataBuffer data);
    void SetPixel(int x, int y, float* fArray, int fArrayc, DataBuffer data);
    void SetPixel(int x, int y, int* iArray, int iArrayc, DataBuffer data);
    void SetPixels(int x, int y, int w, int h, double* dArray, int dArrayc, DataBuffer data);
    void SetPixels(int x, int y, int w, int h, float* fArray, int fArrayc, DataBuffer data);
    void SetPixels(int x, int y, int w, int h, int* iArray, int iArrayc, DataBuffer data);
    void SetSample(int x, int y, int b, double s, DataBuffer data);
    void SetSample(int x, int y, int b, float s, DataBuffer data);
    void SetSample(int x, int y, int b, int s, DataBuffer data);
    void SetSamples(int x, int y, int w, int h, int b, double* dArray, int dArrayc, DataBuffer data);
    void SetSamples(int x, int y, int w, int h, int b, float* fArray, int fArrayc, DataBuffer data);
    void SetSamples(int x, int y, int w, int h, int b, int* iArray, int iArrayc, DataBuffer data);

public:
    jobject instance;

};

class Raster : public lang::Object {
public:
    Raster(jobject instance);
    ~Raster();

protected:
    Raster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin);
    Raster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, Raster parent);
    Raster(SampleModel sampleModel, Point origin);

public:
    static WritableRaster CreateBandedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int* bankIndices, int bankIndicesc, int* bandOffsets, int bandOffsetsc, Point location);
    static WritableRaster CreateBandedRaster(int dataType, int w, int h, int scanlineStride, int* bankIndices, int bankIndicesc, int* bandOffsets, int bandOffsetsc, Point location);
    static WritableRaster CreateBandedRaster(int dataType, int w, int h, int bands, Point location);
    Raster CreateChild(int parentX, int parentY, int width, int height, int childMinX, int childMinY, int* bandList, int bandListc);
    WritableRaster CreateCompatibleWritableRaster();
    WritableRaster CreateCompatibleWritableRaster(int w, int h);
    WritableRaster CreateCompatibleWritableRaster(int x, int y, int w, int h);
    WritableRaster CreateCompatibleWritableRaster(Rectangle rect);
    static WritableRaster CreateInterleavedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int pixelStride, int* bandOffsets, int bandOffsetsc, Point location);
    static WritableRaster CreateInterleavedRaster(int dataType, int w, int h, int scanlineStride, int pixelStride, int* bandOffsets, int bandOffsetsc, Point location);
    static WritableRaster CreateInterleavedRaster(int dataType, int w, int h, int bands, Point location);
    static WritableRaster CreatePackedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int* bandMasks, int bandMasksc, Point location);
    static WritableRaster CreatePackedRaster(DataBuffer dataBuffer, int w, int h, int bitsPerPixel, Point location);
    static WritableRaster CreatePackedRaster(int dataType, int w, int h, int* bandMasks, int bandMasksc, Point location);
    static WritableRaster CreatePackedRaster(int dataType, int w, int h, int bands, int bitsPerBand, Point location);
    static Raster CreateRaster(SampleModel sm, DataBuffer db, Point location);
    Raster CreateTranslatedChild(int childMinX, int childMinY);
    static WritableRaster CreateWritableRaster(SampleModel sm, DataBuffer db, Point location);
    static WritableRaster CreateWritableRaster(SampleModel sm, Point location);
    Rectangle GetBounds();
    DataBuffer GetDataBuffer();
    lang::Object GetDataElements(int x, int y, int w, int h, lang::Object outData);
    lang::Object GetDataElements(int x, int y, lang::Object outData);
    int GetHeight();
    int GetMinX();
    int GetMinY();
    int GetNumBands();
    int GetNumDataElements();
    Raster GetParent();
    double* GetPixel(int* size, int x, int y, double* dArray, int dArrayc);
    float* GetPixel(int* size, int x, int y, float* fArray, int fArrayc);
    int* GetPixel(int* size, int x, int y, int* iArray, int iArrayc);
    double* GetPixels(int* size, int x, int y, int w, int h, double* dArray, int dArrayc);
    float* GetPixels(int* size, int x, int y, int w, int h, float* fArray, int fArrayc);
    int* GetPixels(int* size, int x, int y, int w, int h, int* iArray, int iArrayc);
    int GetSample(int x, int y, int b);
    double GetSampleDouble(int x, int y, int b);
    float GetSampleFloat(int x, int y, int b);
    SampleModel GetSampleModel();
    int GetSampleModelTranslateX();
    int GetSampleModelTranslateY();
    double* GetSamples(int* size, int x, int y, int w, int h, int b, double* dArray, int dArrayc);
    float* GetSamples(int* size, int x, int y, int w, int h, int b, float* fArray, int fArrayc);
    int* GetSamples(int* size, int x, int y, int w, int h, int b, int* iArray, int iArrayc);
    int GetTransferType();
    int GetWidth();

public:
    jobject instance;

};

class WritableRaster : public Raster {
public:
    WritableRaster(jobject instance);
    ~WritableRaster();

protected:
    WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin);
    WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, WritableRaster parent);
    WritableRaster(SampleModel sampleModel, Point origin);

public:
    WritableRaster CreateWritableChild(int parentX, int parentY, int w, int h, int childMinX, int childMinY, int* bandList, int bandListc);
    WritableRaster CreateWritableTranslatedChild(int childMinX, int childMinY);
    WritableRaster GetWritableParent();
    void SetDataElements(int x, int y, int w, int h, lang::Object inData);
    void SetDataElements(int x, int y, lang::Object inData);
    void SetDataElements(int x, int y, Raster inRaster);
    void SetPixel(int x, int y, double* dArray, int dArrayc);
    void SetPixel(int x, int y, float* fArray, int fArrayc);
    void SetPixel(int x, int y, int* iArray, int iArrayc);
    void SetPixels(int x, int y, int w, int h, double* dArray, int dArrayc);
    void SetPixels(int x, int y, int w, int h, float* fArray, int fArrayc);
    void SetPixels(int x, int y, int w, int h, int* iArray, int iArrayc);
    void SetRect(int dx, int dy, Raster srcRaster);
    void SetRect(Raster srcRaster);
    void SetSample(int x, int y, int b, double s);
    void SetSample(int x, int y, int b, float s);
    void SetSample(int x, int y, int b, int s);
    void SetSamples(int x, int y, int w, int h, int b, double* dArray, int dArrayc);
    void SetSamples(int x, int y, int w, int h, int b, float* fArray, int fArrayc);
    void SetSamples(int x, int y, int w, int h, int b, int* iArray, int iArrayc);

public:
    jobject instance;

};

class ColorModel : public lang::Object, public Transparency {
public:
    ColorModel(jobject instance);
    ~ColorModel();

public:
    ColorModel CoerceData(WritableRaster raster, bool isAlphaPremultiplied);
    SampleModel CreateCompatibleSampleModel(int w, int h);
    WritableRaster CreateCompatibleWritableRaster(int w, int h);
    bool Equals(lang::Object obj);
    void Finalize();
    int GetAlpha(int pixel);
    int GetAlpha(lang::Object inData);
    WritableRaster GetAlphaRaster(WritableRaster raster);
    int GetBlue(int pixel);
    int GetBlue(lang::Object inData);
    color::ColorSpace GetColorSpace();
    int* GetComponents(int* size, int pixel, int* components, int componentsc, int offset);
    int* GetComponents(int* size, lang::Object pixel, int* components, int componentsc, int offset);
    int* GetComponentSize(int* size);
    int GetComponentSize(int componentIdx);
    int GetDataElement(float* normComponents, int normComponentsc, int normOffset);
    int GetDataElement(int* components, int componentsc, int offset);
    lang::Object GetDataElements(float* normComponents, int normComponentsc, int normOffset, lang::Object obj);
    lang::Object GetDataElements(int* components, int componentsc, int offset, lang::Object obj);
    lang::Object GetDataElements(int rgb, lang::Object pixel);
    int GetGreen(int pixel);
    int GetGreen(lang::Object inData);
    float* GetNormalizedComponents(int* size, int* components, int componentsc, int offset, float* normComponents, int normComponentsc, int normOffset);
    float* GetNormalizedComponents(int* size, lang::Object pixel, float* normComponents, int normComponentsc, int normOffset);
    int GetNumColorComponents();
    int GetNumComponents();
    int GetPixelSize();
    int GetRed(int pixel);
    int GetRed(lang::Object inData);
    int GetRGB(int pixel);
    int GetRGB(lang::Object inData);
    static ColorModel GetRGBdefault();
    int GetTransferType();
    int GetTransparency();
    int* GetUnnormalizedComponents(int* size, float* normComponents, int normComponentsc, int normOffset, int* components, int componentsc, int offset);
    bool HasAlpha();
    int HashCode();
    bool IsAlphaPremultiplied();
    bool IsCompatibleRaster(Raster raster);
    bool IsCompatibleSampleModel(SampleModel sm);
    char* ToString();

public:
    jobject instance;

};

class IndexColorModel : public ColorModel {
public:
    IndexColorModel(jobject instance);
    ~IndexColorModel();

public:
    IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc);
    IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc, signed char* a, int ac);
    IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc, int trans);
    IndexColorModel(int bits, int size, signed char* cmap, int cmapc, int start, bool hasalpha);
    IndexColorModel(int bits, int size, signed char* cmap, int cmapc, int start, bool hasalpha, int trans);
    IndexColorModel(int bits, int size, int* cmap, int cmapc, int start, bool hasalpha, int trans, int transferType);
    IndexColorModel(int bits, int size, int* cmap, int cmapc, int start, int transferType, math::BigInteger validBits);

public:
    BufferedImage ConvertToIntDiscrete(Raster raster, bool forceARGB);
    SampleModel CreateCompatibleSampleModel(int w, int h);
    WritableRaster CreateCompatibleWritableRaster(int w, int h);
    void Finalize();
    int GetAlpha(int pixel);
    void GetAlphas(signed char* a, int ac);
    int GetBlue(int pixel);
    void GetBlues(signed char* b, int bc);
    int* GetComponents(int* size, int pixel, int* components, int componentsc, int offset);
    int* GetComponents(int* size, lang::Object pixel, int* components, int componentsc, int offset);
    int* GetComponentSize(int* size);
    int GetDataElement(int* components, int componentsc, int offset);
    lang::Object GetDataElements(int* components, int componentsc, int offset, lang::Object obj);
    lang::Object GetDataElements(int rgb, lang::Object pixel);
    int GetGreen(int pixel);
    void GetGreens(signed char* g, int gc);
    int GetMapSize();
    int GetRed(int pixel);
    void GetReds(signed char* r, int rc);
    int GetRGB(int pixel);
    void GetRGBs(int* rgb, int rgbc);
    int GetTransparency();
    int GetTransparentPixel();
    math::BigInteger GetValidPixels();
    bool IsCompatibleRaster(Raster raster);
    bool IsCompatibleSampleModel(SampleModel sm);
    bool IsValid();
    bool IsValid(int pixel);
    char* ToString();

public:
    jobject instance;

};

class RenderedImage : public NativeObjectInstance {
public:
    RenderedImage(jobject instance);
    ~RenderedImage();

public:
    WritableRaster CopyData(WritableRaster raster);
    ColorModel GetColorModel();
    Raster GetData();
    Raster GetData(Rectangle rect);
    int GetHeight();
    int GetMinTileX();
    int GetMinTileY();
    int GetMinX();
    int GetMinY();
    int GetNumXTiles();
    int GetNumYTiles();
    lang::Object GetProperty(const char* name);
    char** GetPropertyNames(int* size);
    SampleModel GetSampleModel();
    util::Vector GetSources();
    Raster GetTile(int tileX, int tileY);
    int GetTileGridXOffset();
    int GetTileGridYOffset();
    int GetTileHeight();
    int GetTileWidth();
    int GetWidth();

public:
    jobject instance;

};

class TileObserver : public NativeObjectInstance {
public:
    TileObserver(jobject instance);
    ~TileObserver();

public:
    void TileUpdate(WritableRenderedImage source, int tileX, int tileY, bool willBeWritable);

public:
    jobject instance;

};

class ImageObserver : public NativeObjectInstance {
public:
    ImageObserver(jobject instance);
    ~ImageObserver();

public:
    const static int ABORT = 128;
    const static int ALLBITS = 32;
    const static int ERROR = 64;
    const static int FRAMEBITS = 16;
    const static int HEIGHT = 2;
    const static int PROPERTIES = 4;
    const static int SOMEBITS = 8;
    const static int WIDTH = 1;

public:
    bool ImageUpdate(Image img, int infoflags, int x, int y, int width, int height);

public:
    jobject instance;

};

class ImageConsumer : public NativeObjectInstance {
public:
    ImageConsumer(jobject instance);
    ~ImageConsumer();

public:
    const static int COMPLETESCANLINES = 4;
    const static int IMAGEABORTED = 4;
    const static int IMAGEERROR = 1;
    const static int RANDOMPIXELORDER = 1;
    const static int SINGLEFRAME = 16;
    const static int SINGLEFRAMEDONE = 2;
    const static int SINGLEPASS = 8;
    const static int STATICIMAGEDONE = 3;
    const static int TOPDOWNLEFTRIGHT = 2;

public:
    void ImageComplete(int status);
    void SetColorModel(ColorModel model);
    void SetDimensions(int width, int height);
    void SetHints(int hitflags);
    void SetPixels(int x, int y, int w, int h, ColorModel model, signed char* pixels, int pixelsc, int off, int scansize);
    void SetPixels(int x, int y, int w, int h, ColorModel model, int* pixels, int pixelsc, int off, int scansize);
    void SetProperties(util::Hashtable props);

public:
    jobject instance;

};

class ImageProducer : public NativeObjectInstance {
public:
    ImageProducer(jobject instance);
    ~ImageProducer();

public:
    void AddConsumer(ImageConsumer ic);
    bool IsConsumer(ImageConsumer ic);
    void RemoveConsumer(ImageConsumer ic);
    void RequestTopDownLeftRightResend(ImageConsumer ic);
    void StartProduction(ImageConsumer ic);

public:
    jobject instance;

};

class WritableRenderedImage : public RenderedImage {
public:
    WritableRenderedImage(jobject instance);
    ~WritableRenderedImage();

public:
    void AddTileObserver(TileObserver to);
    WritableRaster GetWritableTile(int tileX, int tileY);
    Point* GetWritableTileIndices(int* size);
    bool HasTileWriters();
    bool IsTileWritable(int tileX, int tileY);
    void ReleaseWritableTile(int tileX, int tileY);
    void RemoveTileObserver(TileObserver to);
    void SetData(Raster r);

public:
    jobject instance;

};

}

class ImageCapabilities : public lang::Object {
public:
    ImageCapabilities(jobject instance);
    ImageCapabilities(bool accelerated);
    ~ImageCapabilities();

public:
    lang::Object Clone();
    bool IsAccelerated();
    bool IsTrueVolatile();

public:
    jobject instance;

};

class BufferCapabilities : public lang::Object {
public:
    class FlipContents;

public:
    BufferCapabilities(jobject instance);
    ~BufferCapabilities();

public:
    BufferCapabilities(ImageCapabilities frontCaps, ImageCapabilities backCaps, BufferCapabilities::FlipContents flipContents);

public:
    lang::Object Clone();
    ImageCapabilities GetBackBufferCapabilities();
    BufferCapabilities::FlipContents GetFlipContents();
    ImageCapabilities GetFrontBufferCapabilities();
    bool IsFullScreenRequired();
    bool IsMultiBufferAvailable();
    bool IsPageFlipping();

public:
    jobject instance;

};

class BufferCapabilities::FlipContents : public lang::Object {
public:
    FlipContents(jobject instance);
    ~FlipContents();

public:
    __FIELD__ static BufferCapabilities::FlipContents BACKGROUND();
    __FIELD__ static BufferCapabilities::FlipContents COPIED();
    __FIELD__ static BufferCapabilities::FlipContents PRIOR();
    __FIELD__ static BufferCapabilities::FlipContents UNDEFINED();

public:
    int HashCode();
    char* ToString();

public:
    jobject instance;

};

class GraphicsConfiguration : public lang::Object {
public:
    GraphicsConfiguration(jobject instance);
    ~GraphicsConfiguration();

public:
    image::BufferedImage CreateCompatibleImage(int width, int height);
    image::BufferedImage CreateCompatibleImage(int width, int height, int transparency);
    image::VolatileImage CreateCompatibleVolatileImage(int width, int height);
    image::VolatileImage CreateCompatibleVolatileImage(int width, int height, ImageCapabilities caps);
    image::VolatileImage CreateCompatibleVolatileImage(int width, int height, ImageCapabilities caps, int transparency);
    image::VolatileImage CreateCompatibleVolatileImage(int width, int height, int transparency);
    Rectangle GetBounds();
    BufferCapabilities GetBufferCapabilities();
    image::ColorModel GetColorModel();
    image::ColorModel GetColorModel(int transparency);
    geom::AffineTransform GetDefaultTransform();
    ImageCapabilities GetImageCapabilities();
    geom::AffineTransform GetNormalizingTransform();
    bool IsTranslucencyCapable();

public:
    jobject instance;

};

class Image : public lang::Object {
public:
    Image(jobject instance);
    ~Image();

public:
    const static int SCALE_AREA_AVERAGING = 16;
    const static int SCALE_DEFAULT = 1;
    const static int SCALE_FAST = 2;
    const static int SCALE_REPLICATE = 8;
    const static int SCALE_SMOOTH = 4;

public:
    void Flush();
    float GetAccelerationPriority();
    ImageCapabilities GetCapabilities(GraphicsConfiguration gc);
    Graphics GetGraphics();
    int GetHeight(image::ImageObserver observer);
    Image GetScaledInstance(int width, int height, int hints);
    image::ImageProducer GetSource();
    int GetWidth(image::ImageObserver observer);
    void SetAccelerationPriority(float priority);

public:
    jobject instance;

};

namespace image {

class VolatileImage : public Image, public Transparency {
public:
    VolatileImage(jobject instance);
    ~VolatileImage();

public:
    const static int IMAGE_INCOMPATIBLE = 2;
    const static int IMAGE_OK = 0;
    const static int IMAGE_RESTORED = 1;

public:
    bool ContentsLost();
    Graphics2D CreateGraphics();
    ImageCapabilities GetCapabilities();
    Graphics GetGraphics();
    int GetHeight();
    BufferedImage GetSnapshot();
    ImageProducer GetSource();
    int GetTransparency();
    int GetWidth();
    int Validate(GraphicsConfiguration gc);

public:
    jobject instance;

};

class BufferedImage : public Image {
public:
    BufferedImage(jobject instance);
    ~BufferedImage();

public:
    const static int TYPE_3BYTE_BGR = 5;
    const static int TYPE_4BYTE_ABGR = 6;
    const static int TYPE_4BYTE_ABGR_PRE = 7;
    const static int TYPE_BYTE_BINARY = 12;
    const static int TYPE_BYTE_GRAY = 10;
    const static int TYPE_BYTE_INDEXED = 13;
    const static int TYPE_CUSTOM = 0;
    const static int TYPE_INT_ARGB = 2;
    const static int TYPE_INT_ARGB_PRE = 3;
    const static int TYPE_INT_BGR = 4;
    const static int TYPE_INT_RGB = 1;
    const static int TYPE_USHORT_555_RGB = 9;
    const static int TYPE_USHORT_565_RGB = 8;
    const static int TYPE_USHORT_GRAY = 11;

public:
    BufferedImage(ColorModel cm, WritableRaster raster, bool isRasterPremultiplied, util::Hashtable properties);
    BufferedImage(int width, int height, int imageType);
    BufferedImage(int width, int height, int imageType, IndexColorModel cm);

public:
    void AddTileObserver(TileObserver to);
    void CoerceData(bool isAlphaPremultiplied);
    WritableRaster CopyData(WritableRaster outRaster);
    Graphics2D CreateGraphics();
    WritableRaster GetAlphaRaster();
    ColorModel GetColorModel();
    Raster GetData();
    Raster GetData(Rectangle rect);
    Graphics GetGraphics();
    int GetHeight();
    int GetHeight(ImageObserver observer);
    int GetMinTileX();
    int GetMinTileY();
    int GetMinX();
    int GetMinY();
    int GetNumXTiles();
    int GetNumYTiles();
    lang::Object GetProperty(const char* name);
    lang::Object GetProperty(const char* name, ImageObserver observer);
    char** GetPropertyNames(int* size);
    WritableRaster GetRaster();
    int GetRGB(int x, int y);
    int* GetRGB(int* size, int startX, int startY, int w, int h, int* rgbArray, int rgbArrayc, int offset, int scansize);
    SampleModel GetSampleModel();
    ImageProducer GetSource();
    util::Vector GetSources();
    BufferedImage GetSubimage(int x, int y, int w, int h);
    Raster GetTile(int tileX, int tileY);
    int GetTileGridXOffset();
    int GetTileGridYOffset();
    int GetTileHeight();
    int GetTileWidth();
    int GetTransparency();
    int GetType();
    int GetWidth();
    int GetWidth(ImageObserver observer);
    WritableRaster GetWritableTile(int tileX, int tileY);
    Point* GetWritableTileIndices(int* size);
    bool HasTileWriters();
    bool IsAlphaPremultiplied();
    bool IsTileWritable(int tileX, int tileY);
    void RemoveTileObserver(TileObserver to);
    void SetData(Raster r);
    void SetRGB(int x, int y, int rgb);
    void SetRGB(int startX, int startY, int w, int g, int* rgbArray, int rgbArrayc, int offset, int scansize);
    char* ToString();
    
public:
    jobject instance;

};

class BufferedImageOp : public NativeObjectInstance {
public:
    BufferedImageOp(jobject instance);
    ~BufferedImageOp();

public:
    BufferedImage CreateCompatibleDestImage(BufferedImage src, ColorModel destCM);
    BufferedImage Filter(BufferedImage src, BufferedImage desc);
    geom::Rectangle2D GetBounds2D(BufferedImage src);
    geom::Point2D GetPoint2D(geom::Point2D srcPt, geom::Point2D dstPt);
    RenderingHints GetRenderingHints();

public:
    jobject instance;

};

}

class PaintContext : public NativeObjectInstance {
public:
    PaintContext(jobject instance);
    ~PaintContext();

public:
    void Dispose();
    image::ColorModel GetColorModel();
    image::Raster GetRaster(int x, int y, int w, int h);
    
public:
    jobject instance;

};

class RenderingHints : public lang::Object, public util::Map {
public:
    class Key;

public:
    RenderingHints(jobject instance);
    ~RenderingHints();

public:
    RenderingHints(Map init);
    RenderingHints(RenderingHints::Key key, lang::Object value);

public:
    void Add(RenderingHints hints);
    void Clear();
    lang::Object Clone();
    bool ContainsKey(lang::Object key);
    bool ContainsValue(lang::Object value);
    util::Set EntrySet();
    bool Equals(lang::Object o);
    lang::Object Get(lang::Object key);
    int HashCode();
    bool IsEmpty();
    util::Set KeySet();
    lang::Object Put(lang::Object key, lang::Object value);
    void PutAll(Map m);
    lang::Object Remove(lang::Object key);
    int Size();
    char* ToString();
    util::Collection Values();

public:
    __FIELD__ static RenderingHints::Key KEY_ALPHA_INTERPOLATION();
    __FIELD__ static RenderingHints::Key KEY_ANTIALIASING();
    __FIELD__ static RenderingHints::Key KEY_COLOR_RENDERING();
    __FIELD__ static RenderingHints::Key KEY_DITHERING();
    __FIELD__ static RenderingHints::Key KEY_FRACTIONALMETRICS();
    __FIELD__ static RenderingHints::Key KEY_INTERPOLATION();
    __FIELD__ static RenderingHints::Key KEY_RENDERING();
    __FIELD__ static RenderingHints::Key KEY_STROKE_CONTROL();
    __FIELD__ static RenderingHints::Key KEY_TEXT_ANTIALIASING();
    __FIELD__ static RenderingHints::Key KEY_TEXT_LCD_CONTRAST();
    __FIELD__ static lang::Object VALUE_ALPHA_INTERPOLATION_DEFAULT();
    __FIELD__ static lang::Object VALUE_ALPHA_INTERPOLATION_QUALITY();
    __FIELD__ static lang::Object VALUE_ALPHA_INTERPOLATION_SPEED();
    __FIELD__ static lang::Object VALUE_ANTIALIAS_DEFAULT();
    __FIELD__ static lang::Object VALUE_ANTIALIAS_OFF();
    __FIELD__ static lang::Object VALUE_ANTIALIAS_ON();
    __FIELD__ static lang::Object VALUE_COLOR_RENDER_DEFAULT();
    __FIELD__ static lang::Object VALUE_COLOR_RENDER_QUALITY();
    __FIELD__ static lang::Object VALUE_COLOR_RENDER_SPEED();
    __FIELD__ static lang::Object VALUE_DITHER_DEFAULT();
    __FIELD__ static lang::Object VALUE_DITHER_DISABLE();
    __FIELD__ static lang::Object VALUE_DITHER_ENABLE();
    __FIELD__ static lang::Object VALUE_FRACTIONALMETRICS_DEFAULT();
    __FIELD__ static lang::Object VALUE_FRACTIONALMETRICS_OFF();
    __FIELD__ static lang::Object VALUE_FRACTIONALMETRICS_ON();
    __FIELD__ static lang::Object VALUE_INTERPOLATION_BICUBIC();
    __FIELD__ static lang::Object VALUE_INTERPOLATION_BILINEAR();
    __FIELD__ static lang::Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR();
    __FIELD__ static lang::Object VALUE_RENDER_DEFAULT();
    __FIELD__ static lang::Object VALUE_RENDER_QUALITY();
    __FIELD__ static lang::Object VALUE_RENDER_SPEED();
    __FIELD__ static lang::Object VALUE_STROKE_DEFAULT();
    __FIELD__ static lang::Object VALUE_STROKE_NORMALIZE();
    __FIELD__ static lang::Object VALUE_STROKE_PURE();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_DEFAULT();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_GASP();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_LCD_HBGR();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_LCD_HRGB();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_LCD_VBGR();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_LCD_VRGB();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_OFF();
    __FIELD__ static lang::Object VALUE_TEXT_ANTIALIAS_ON();

public:
    jobject instance;

};

class RenderingHints::Key : public lang::Object {
public:
    Key(jobject instance);
    ~Key();

public:
    bool Equals(lang::Object o);
    int HashCode();
    bool IsCompatibleValue(lang::Object val);
    
public:
    jobject instance;

};

class Paint : public Transparency {
public:
    Paint(jobject instance);
    ~Paint();

public:
    PaintContext CreateContext(image::ColorModel cm, Rectangle deviceBounds, geom::Rectangle2D userBounds, geom::AffineTransform xform, RenderingHints hints);

public:
    jobject instance;

};

class Color : public lang::Object, public Paint {
public:
    Color(jobject instance);
    ~Color();

public:
    Color(color::ColorSpace cspace, float* components, int componentsc, float alpha);
    Color(float r, float g, float b);
    Color(float r, float g, float b, float a);
    Color(int rgb);
    Color(int rgba, bool hasalpha);
    Color(int r, int g, int b);
    Color(int r, int g, int b, int a);

public:
    Color Brighter();
    PaintContext CreateContext(image::ColorModel cm, Rectangle r, geom::Rectangle2D r2d, geom::AffineTransform xform, RenderingHints hints);
    Color Darker();
    static Color Decode(const char* nm);
    bool Equals(lang::Object obj);
    int GetAlpha();
    int GetBlue();
    static Color GetColor(const char* nm);
    static Color GetColor(const char* nm, Color v);
    static Color GetColor(const char* nm, int v);
    float* GetColorComponents(int* size, color::ColorSpace cspace, float* compArray, int compArrayc);
    float* GetColorComponents(int* size, float* compArray, int compArrayc);
    color::ColorSpace GetColorSpace();
    float* GetComponents(int* size, color::ColorSpace cspace, float* compArray, int compArrayc);
    float* GetComponents(int* size, float* compArray, int compArrayc);
    int GetGreen();
    static Color GetHSBColor(float h, float s, float b);
    int GetRed();
    int GetRGB();
    float* GetRGBColorComponents(int* size, float* compArray, int compArrayc);
    float* GetRGBComponents(int* size, float* compArray, int compArrayc);
    int GetTransparency();
    int HashCode();
    static int HSBtoRGB(float hue, float saturation, float brightness);
    static float* RGBtoHSB(int* size, int r, int g, int b, float* hsbvals, int hsbvalsc);
    char* ToString();

public:
    __FIELD__ static Color Black();
    __FIELD__ static Color Blue();
    __FIELD__ static Color Cyan();
    __FIELD__ static Color DarkGray();
    __FIELD__ static Color Gray();
    __FIELD__ static Color Green();
    __FIELD__ static Color LightGray();
    __FIELD__ static Color Magenta();
    __FIELD__ static Color Orange();
    __FIELD__ static Color Pink();
    __FIELD__ static Color Red();
    __FIELD__ static Color White();
    __FIELD__ static Color Yellow();
    
public:
    jobject instance;

};

class Stroke : public NativeObjectInstance {
public:
    Stroke(jobject instance);
    ~Stroke();

public:
    Shape CreateStrokedShape(Shape p);

public:
    jobject instance;

};

class CompositeContext : public NativeObjectInstance {
public:
    CompositeContext(jobject instance);
    ~CompositeContext();

public:
    void Compose(image::Raster src, image::Raster dstIn, image::WritableRaster dstOut);
    void Dispose();
    
public:
    jobject instance;

};

class Composite : public NativeObjectInstance {
public:
    Composite(jobject instance);
    ~Composite();

public:
    CompositeContext CreateContext(image::ColorModel srcColorModel, image::ColorModel dstColorModel, RenderingHints hints);

public:
    jobject instance;

};

namespace font {

class GlyphJustificationInfo : public lang::Object {
public:
    GlyphJustificationInfo(jobject instance);
    ~GlyphJustificationInfo();

public:
    GlyphJustificationInfo(float weight, bool growAbsorb, int growPriority, float growLeftLimit, float growRightLimit, bool shrinkAbsorb, int shrinkPriority, float shrinkLeftLimit, float shrinkRightLimit);

public:
    const static int PRIORITY_INTERCHAR = 2;
    const static int PRIORITY_KASHIDA = 0;
    const static int PRIORITY_NONE = 3;
    const static int PRIORITY_WHITESPACE = 1;

public:
    __FIELD__ bool GrowAbsorb();
    __FIELD__ float GrowLeftLimit();
    __FIELD__ int GrowPriority();
    __FIELD__ float GrowRightLimit();
    __FIELD__ bool ShrinkAbsorb();
    __FIELD__ float ShrinkLeftLimit();
    __FIELD__ int ShrinkPriority();
    __FIELD__ float ShrinkRightLimit();
    __FIELD__ float Weight();

public:
    jobject instance;

};

class FontRenderContext : public lang::Object {
public:
    FontRenderContext(jobject instance);
    ~FontRenderContext();

public:
    FontRenderContext(geom::AffineTransform tx, bool isAntiAliased, bool usesFractionalMetrics);
    FontRenderContext(geom::AffineTransform tx, lang::Object aaHint, lang::Object fmHint);

public:
    bool Equals(FontRenderContext rhs);
    bool Equals(lang::Object obj);
    lang::Object GetAntiAliasingHint();
    lang::Object GetFractionalMetricsHint();
    geom::AffineTransform GetTransform();
    int GetTransformType();
    int HashCode();
    bool IsAntiAliased();
    bool IsTransformed();
    bool UsesFractionalMetrics();
    
public:
    jobject instance;

};

class LineMetrics : public lang::Object {
public:
    LineMetrics(jobject instance);
    ~LineMetrics();

public:
    float GetAscent();
    int GetBaselineIndex();
    float* GetBaselineOffsets(int* size);
    float GetDescent();
    float GetHeight();
    float GetLeading();
    int GetNumChars();
    float GetStrikethroughOffset();
    float GetStrikethroughThickness();
    float GetUnderlineOffset();
    float GetUnderlineThickness();
    
public:
    jobject instance;

};

class GlyphMetrics : public lang::Object {
public:
    GlyphMetrics(jobject instance);
    ~GlyphMetrics();

public:
    GlyphMetrics(bool horizontal, float advanceX, float advanceY, geom::Rectangle2D bounds, signed char glyphType);
    GlyphMetrics(float advance, geom::Rectangle2D bounds, signed char glyphType);

public:
    const static signed char COMBINING = 2;
    const static signed char COMPONENT = 3;
    const static signed char LIGATURE = 1;
    const static signed char STANDARD = 0;
    const static signed char WHITESPACE = 4;

public:
    float GetAdvance();
    float GetAdvanceX();
    float GetAdvanceY();
    geom::Rectangle2D GetBounds2D();
    float GetLSB();
    float GetRSB();
    int GetType();
    bool IsCombining();
    bool IsComponent();
    bool IsLigature();
    bool IsStandard();
    bool IsWhitespace();
    
public:
    jobject instance;

};

class GlyphVector : public lang::Object {
public:
    GlyphVector(jobject instance);
    ~GlyphVector();

public:
    const static int FLAG_COMPLEX_GLYPHS = 8;
    const static int FLAG_HAS_POSITION_ADJUSTMENTS = 2;
    const static int FLAG_HAS_TRANSFORMS = 1;
    const static int FLAG_MASK = 15;
    const static int FLAG_RUN_RTL = 4;

public:
    bool Equals(GlyphVector set);
    Font GetFont();
    FontRenderContext GetFontRenderContext();
    int GetGlyphCharIndex(int glyphIndex);
    int* GetGlyphCharIndices(int* size, int beginGlyphIndex, int numEntries, int* codeReturn, int codeReturnc);
    int GetGlyphCode(int glyphIndex);
    int* GetGlyphCodes(int* size, int beginGlyphIndex, int numEntries, int* codeReturn, int codeReturnc);
    GlyphJustificationInfo GetGlyphJustificationInfo(int glyphIndex);
    Shape GetGlyphLogicalBounds(int glyphIndex);
    GlyphMetrics GetGlyphMetrics(int glyphIndex);
    Shape GetGlyphOutline(int glyphIndex);
    Shape GetGlyphOutline(int glyphIndex, float x, float y);
    Rectangle GetGlyphPixelBounds(int index, FontRenderContext renderFRC, float x, float y);
    geom::Point2D GetGlyphPosition(int glyphIndex);
    float* GetGlyphPositions(int* size, int beginGlyphIndex, int numEntries, float* positionReturn, int positionReturnc);
    geom::AffineTransform GetGlyphTransform(int glyphIndex);
    Shape GetGlyphVisualBounds(int glyphIndex);
    int GetLayoutFlags();
    geom::Rectangle2D GetLogicalBounds();
    int GetNumGlyphs();
    Shape GetOutline();
    Shape GetOutline(float x, float y);
    Rectangle GetPixelBounds(FontRenderContext renderFRC, float x, float y);
    geom::Rectangle2D GetVisualBounds();
    void PerformDefaultLayout();
    void SetGlyphPosition(int glyphIndex, geom::Point2D newPos);
    void SetGlyphTransform(int glyphIndex, geom::AffineTransform newTX);
    
public:
    jobject instance;

};

}

class Font : public lang::Object {
public:
    Font(jobject instance);
    ~Font();

public:
    Font(util::Map attributes);
    Font(const char* name, int style, int size);

public: 
    const static int BOLD = 1;
    const static int CENTER_BASELINE = 1;
    static constexpr const char* const DIALOG = "Dialog";
    static constexpr const char* const DIALOG_INPUT = "DialogInput";
    const static int HANGING_BASELINE = 2;
    const static int ITALIC = 2;
    const static int LAYOUT_LEFT_TO_RIGHT = 0;
    const static int LAYOUT_NO_LIMIT_CONTEXT = 4;
    const static int LAYOUT_NO_START_CONTEXT = 2;
    const static int LAYOUT_RIGHT_TO_LEFT = 1;
    static constexpr const char* const MONOSPACED = "Monospaced";
    const static int PLAIN = 0;
    const static int ROMAN_BASELINE = 0;
    static constexpr const char* const SANS_SERIF = "SansSerif";
    static constexpr const char* const SERIF = "Serif";
    const static int TRUETYPE_FONT = 0;
    const static int TYPE1_FONT = 1;

public:
    bool CanDisplay(char c);
    bool CanDisplay(int codePoint);
    int CanDisplayUpTo(const char* text, int textc, int start, int limit);
    int CanDisplayUpTo(text::CharacterIterator iter, int start, int limit);
    int CanDisplayUpTo(const char* str);
    static Font CreateFont(int fontFormat, io::File fontFile);
    static Font CreateFont(int fontFormat, io::InputStream fontStream);
    font::GlyphVector CreateGlyphVector(font::FontRenderContext frc, const char* chars, int charsc);
    font::GlyphVector CreateGlyphVector(font::FontRenderContext frc, text::CharacterIterator ci);
    font::GlyphVector CreateGlyphVector(font::FontRenderContext frc, int* glyphCodes, int glyphCodesc);
    font::GlyphVector CreateGlyphVector(font::FontRenderContext frc, const char* str);
    static Font Decode(const char* str);
    Font DeriveFont(geom::AffineTransform trans);
    Font DeriveFont(float size);
    Font DeriveFont(int style);
    Font DeriveFont(int style, geom::AffineTransform trans);
    Font DeriveFont(int style, float size);
    Font DeriveFont(util::Map attributes);
    bool Equals(lang::Object obj);
    util::Map GetAttributes();
    text::AttributedCharacterIterator::Attribute* GetAvailableAttributes(int* size);
    signed char GetBaselineFor(char c);
    char* GetFamily();
    static Font GetFont(util::Map map);
    static Font GetFont(const char* nm);
    static Font GetFont(const char* nm, Font font);
    char* GetFontName();
    float GetItalicAngle();
    font::LineMetrics GetLineMetrics(const char* chars, int charsc, int beginIndex, int limit, font::FontRenderContext frc);
    font::LineMetrics GetLineMetrics(text::CharacterIterator ci, int beginIndex, int limit, font::FontRenderContext frc);
    font::LineMetrics GetLineMetrics(const char* str, font::FontRenderContext frc);
    font::LineMetrics GetLineMetrics(const char* str, int beginIndex, int limit, font::FontRenderContext frc);
    geom::Rectangle2D GetMaxCharBounds(font::FontRenderContext frc);
    int GetMissingGlyphCode();
    char* GetName();
    int GetNumGlyphs();
    char* GetPSName();
    int GetSize();
    float GetSize2D();
    geom::Rectangle2D GetStringBounds(const char* chars, int charsc, int beginIndex, int limit, font::FontRenderContext frc);
    geom::Rectangle2D GetStringBounds(text::CharacterIterator ci, int beginIndex, int limit, font::FontRenderContext frc);
    geom::Rectangle2D GetStringBounds(const char* str, font::FontRenderContext frc);
    geom::Rectangle2D GetStringBounds(const char* str, int beginIndex, int limit, font::FontRenderContext frc);
    int GetStyle();
    geom::AffineTransform GetTransform();
    int HashCode();
    bool HasLayoutAttributes();
    bool HasUniformLineMetrics();
    bool IsBold();
    bool IsItalic();
    bool IsPlain();
    bool IsTransformed();
    font::GlyphVector LayoutGlyphVector(font::FontRenderContext frc, const char* text, int textc, int start, int limit, int flags);
    char* ToString();
    
public:
    jobject instance;

};

class FontMetrics : public lang::Object {
public:
    FontMetrics(jobject instance);
    ~FontMetrics();

public:
    int BytesWidth(signed char* data, int datac, int off, int len);
    int CharsWidth(char* data, int datac, int off, int len);
    int CharWidth(char ch);
    int CharWidth(int codePoint);
    int GetAscent();
    int GetDescent();
    Font GetFont();
    font::FontRenderContext GetFontRenderContext();
    int GetHeight();
    int GetLeading();
    font::LineMetrics GetLineMetrics(const char* chars, int charsc, int beginIndex, int limit, Graphics context);
    font::LineMetrics GetLineMetrics(text::CharacterIterator ci, int beginIndex, int limit, Graphics context);
    font::LineMetrics GetLineMetrics(const char* str, Graphics context);
    font::LineMetrics GetLineMetrics(const char* str, int beginIndex, int limit, Graphics context);
    int GetMaxAdvance();
    int GetMaxAscent();
    geom::Rectangle2D GetMaxCharBounds(Graphics context);
    int GetMaxDescent();
    geom::Rectangle2D GetStringBounds(const char* chars, int charsc, int beginIndex, int limit, Graphics context);
    geom::Rectangle2D GetStringBounds(text::CharacterIterator ci, int beginIndex, int limit, Graphics context);
    geom::Rectangle2D GetStringBounds(const char* str, Graphics context);
    geom::Rectangle2D GetStringBounds(const char* str, int beginIndex, int limit, Graphics context);
    int* GetWidths(int* size);
    bool HasUniformLineMetrics();
    int StringWidth(const char* str);
    char* ToString();
    
public:
    jobject instance;

};

class Graphics : public lang::Object {
public:
    Graphics(jobject instance);
    ~Graphics();

public:
    void ClearRect(int x, int y, int width, int height);
    void ClipRect(int x, int y, int width, int height);
    void CopyArea(int x, int y, int width, int height, int dx, int dy);
    Graphics Create();
    Graphics Create(int x, int y, int width, int height);
    void Dispose();
    void Draw3DRect(int x, int y, int width, int height, bool raised);
    void DrawArc(int x, int y, int width, int height, int startAngle, int arcAngle);
    void DrawBytes(signed char* data, int datac, int offset, int length, int x, int y);
    void DrawChars(char* data, int datac, int offset, int length, int x, int y);
    bool DrawImage(Image img, int x, int y, Color bgcolor, image::ImageObserver observer);
    bool DrawImage(Image img, int x, int y, image::ImageObserver observer);
    bool DrawImage(Image img, int x, int y, int width, int height, Color bgcolor, image::ImageObserver observer);
    bool DrawImage(Image img, int x, int y, int width, int height, image::ImageObserver observer);
    bool DrawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, image::ImageObserver observer);
    bool DrawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, image::ImageObserver observer);
    void DrawLine(int x1, int y1, int x2, int y2);
    void DrawOval(int x, int y, int width, int height);
    void DrawPolygon(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints);
    void DrawPolygon(Polygon p);
    void DrawPolyline(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints);
    void DrawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
    void DrawString(text::AttributedCharacterIterator iterator, int x, int y);
    void DrawString(const char* str, int x, int y);
    void Fill3DRect(int x, int y, int width, int height, bool raised);
    void FillArc(int x, int y, int width, int height, int startAngle, int arcAngle);
    void FillOval(int x, int y, int width, int height);
    void FillPolygon(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints);
    void FillPolygon(Polygon p);
    void FillRect(int x, int y, int width, int height);
    void FillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
    void Finalize();
    Shape GetClip();
    Rectangle GetClipBounds();
    Rectangle GetClipBounds(Rectangle r);
    Color GetColor();
    Font GetFont();
    FontMetrics GetFontMetrics();
    FontMetrics GetFontMetrics(Font f);
    bool HitClip(int x, int y, int width, int height);
    void SetClip(int x, int y, int width, int height);
    void SetClip(Shape clip);
    void SetColor(Color c);
    void SetFont(Font font);
    void SetPaintMode();
    void SetXORMode(Color c1);
    char* ToString();
    void Translate(int x, int y);
    
public:
    jobject instance;

};

class Graphics2D : public Graphics {
public:
    Graphics2D(jobject instance);
    ~Graphics2D();

public:
    void AddRenderingHints(util::Map hints);
    void Clip(Shape s);
    void Draw(Shape s);
    void Draw3DRect(int x, int y, int width, int height, bool raised);
    void DrawGlyphVector(font::GlyphVector g, float x, float y);
    void DrawImage(image::BufferedImage img, image::BufferedImageOp op, int x, int y);
    bool DrawImage(Image img, geom::AffineTransform xform, image::ImageObserver obs);
    void DrawRenderableImage(image::renderable::RenderableImage img, geom::AffineTransform xform);
    void DrawRenderedImage(image::RenderedImage img, geom::AffineTransform xform);
    void DrawString(text::AttributedCharacterIterator iterator, float x, float y);
    void DrawString(text::AttributedCharacterIterator iterator, int x, int y);
    void Fill(Shape s);
    void Fill3DRect(int x, int y, int width, int height, bool raised);
    Color GetBackground();
    Composite GetComposite();
    GraphicsConfiguration GetDeviceConfiguration();
    font::FontRenderContext GetFontRenderContext();
    Paint GetPaint();
    lang::Object GetRenderingHint(RenderingHints::Key hintKey);
    RenderingHints GetRenderingHints();
    Stroke GetStroke();
    geom::AffineTransform GetTransform();
    bool Hit(Rectangle rect, Shape shape, bool onStroke);
    void Rotate(double theta);
    void Rotate(double theta, double x, double y);
    void Scale(double sx, double sy);
    void SetBackground(Color color);
    void SetComposite(Composite comp);
    void SetPaint(Paint paint);
    void SetRenderingHint(RenderingHints::Key hintKey, lang::Object hintValue);
    void SetRenderingHints(util::Map hints);
    void SetStroke(Stroke s);
    void SetTransform(geom::AffineTransform Tx);
    void Shear(double shx, double shy);
    void Transform(geom::AffineTransform Tx);
    void Translate(double tx, double ty);
    void Translate(int x, int y);

public:
    jobject instance;

};

}

#endif