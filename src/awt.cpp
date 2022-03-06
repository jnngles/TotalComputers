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

#include "j_env.h"
#include "utils.h"
#include "awt.h"

using namespace awt;
using namespace awt::color;
using namespace awt::font;
using namespace awt::geom;
using namespace awt::image;
using namespace awt::image::renderable;

// java/awt/geom/Dimension2D
// Name: Dimension2D
// Base Class(es): [lang::Object]

NativeObject clsDimension2D;
jclass InitDimension2D() {
   return tcInitNativeObject(clsDimension2D, "java/awt/geom/Dimension2D", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "getHeight", "()D", false }, 
          { "getWidth", "()D", false }, 
          { "setSize", "(Ljava/awt/geom/Dimension2D;)V", false }, 
          { "setSize", "(DD)V", false }
       }, { 
       });
}

Dimension2D::Dimension2D(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitDimension2D();
}

Dimension2D::~Dimension2D() {}

lang::Object Dimension2D::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsDimension2D, 0)));
}

double Dimension2D::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDimension2D, 1));
}

double Dimension2D::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDimension2D, 2));
}

void Dimension2D::SetSize(Dimension2D d) {
   tc::jEnv->CallVoidMethod(METHOD(clsDimension2D, 3), d.instance);
}

void Dimension2D::SetSize(double width, double height) {
   tc::jEnv->CallVoidMethod(METHOD(clsDimension2D, 4), (jdouble)width, (jdouble)height);
}


// java/awt/Dimension
// Name: Dimension
// Base Class(es): [geom::Dimension2D]

NativeObject clsDimension;
jclass InitDimension() {
   return tcInitNativeObject(clsDimension, "java/awt/Dimension", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getHeight", "()D", false }, 
          { "getSize", "()Ljava/awt/Dimension;", false }, 
          { "getWidth", "()D", false }, 
          { "hashCode", "()I", false }, 
          { "setSize", "(Ljava/awt/Dimension;)V", false }, 
          { "setSize", "(DD)V", false }, 
          { "setSize", "(II)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Dimension::Dimension(jobject instance)
 : instance(instance), geom::Dimension2D(instance) {
    InitDimension();
}

Dimension::~Dimension() {}

Dimension::Dimension()
 : instance(tc::jEnv->NewObject(InitDimension(), tc::jEnv->GetMethodID(InitDimension(), "<init>", "()V"))), geom::Dimension2D(instance)
{}

Dimension::Dimension(Dimension& d)
 : instance(tc::jEnv->NewObject(InitDimension(), tc::jEnv->GetMethodID(InitDimension(), "<init>", "(Ljava/awt/Dimension;)V"), d.instance)), geom::Dimension2D(instance)
{}

Dimension::Dimension(int width, int height)
 : instance(tc::jEnv->NewObject(InitDimension(), tc::jEnv->GetMethodID(InitDimension(), "<init>", "(II)V"), (jint)width, (jint)height)), geom::Dimension2D(instance)
{}

bool Dimension::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsDimension, 0), obj.instance);
}

double Dimension::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDimension, 1));
}

Dimension Dimension::GetSize() {
   return (Dimension)(tc::jEnv->CallObjectMethod(METHOD(clsDimension, 2)));
}

double Dimension::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDimension, 3));
}

int Dimension::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDimension, 4));
}

void Dimension::SetSize(Dimension d) {
   tc::jEnv->CallVoidMethod(METHOD(clsDimension, 5), d.instance);
}

void Dimension::SetSize(double width, double height) {
   tc::jEnv->CallVoidMethod(METHOD(clsDimension, 6), (jdouble)width, (jdouble)height);
}

void Dimension::SetSize(int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsDimension, 7), (jint)width, (jint)height);
}

char* Dimension::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsDimension, 8)));
}


// java/awt/geom/AffineTransform
// Name: AffineTransform
// Base Class(es): [lang::Object]

NativeObject clsAffineTransform;
jclass InitAffineTransform() {
   return tcInitNativeObject(clsAffineTransform, "java/awt/geom/AffineTransform", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "concatenate", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "createInverse", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "createTransformedShape", "(Ljava/awt/Shape;)Ljava/awt/Shape;", false }, 
          { "deltaTransform", "([DI[DII)V", false }, 
          { "deltaTransform", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)Ljava/awt/geom/Point2D;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getDeterminant", "()D", false }, 
          { "getMatrix", "([D)V", false }, 
          { "getQuadrantRotateInstance", "(I)Ljava/awt/geom/AffineTransform;", true }, 
          { "getQuadrantRotateInstance", "(IDD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getRotateInstance", "(D)Ljava/awt/geom/AffineTransform;", true }, 
          { "getRotateInstance", "(DD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getRotateInstance", "(DDD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getScaleInstance", "(DD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getScaleX", "()D", false }, 
          { "getScaleY", "()D", false }, 
          { "getShearInstance", "(DD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getShearX", "()D", false }, 
          { "getShearY", "()D", false }, 
          { "getTranslateInstance", "(DD)Ljava/awt/geom/AffineTransform;", true }, 
          { "getTranslateX", "()D", false }, 
          { "getTranslateY", "()D", false }, 
          { "getType", "()I", false }, 
          { "hashCode", "()I", false }, 
          { "inverseTransform", "([DI[DII)V", false }, 
          { "inverseTransform", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)Ljava/awt/geom/Point2D;", false }, 
          { "invert", "()V", false }, 
          { "isIdentity", "()Z", false }, 
          { "preConcatenate", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "quadrantRotate", "(I)V", false }, 
          { "quadrantRotate", "(IDD)V", false }, 
          { "rotate", "(D)V", false }, 
          { "rotate", "(DD)V", false }, 
          { "rotate", "(DDD)V", false }, 
          { "rotate", "(DDDD)V", false }, 
          { "scale", "(DD)V", false }, 
          { "setToIdentity", "()V", false }, 
          { "setToQuadrantRotation", "(I)V", false }, 
          { "setToQuadrantRotation", "(IDD)V", false }, 
          { "setToRotation", "(D)V", false }, 
          { "setToRotation", "(DD)V", false }, 
          { "setToRotation", "(DDD)V", false }, 
          { "setToRotation", "(DDDD)V", false }, 
          { "setToScale", "(DD)V", false }, 
          { "setToShear", "(DD)V", false }, 
          { "setToTranslation", "(DD)V", false }, 
          { "setTransform", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "setTransform", "(DDDDDD)V", false }, 
          { "shear", "(DD)V", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "transform", "([DI[DII)V", false }, 
          { "transform", "([DI[FII)V", false }, 
          { "transform", "([FI[DII)V", false }, 
          { "transform", "([FI[FII)V", false }, 
          { "transform", "([Ljava/awt/geom/Point2D;I[Ljava/awt/geom/Point2D;II)V", false }, 
          { "transform", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)Ljava/awt/geom/Point2D;", false }, 
          { "translate", "(DD)V", false }
       }, { 
       });
}

AffineTransform::AffineTransform(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitAffineTransform();
}

AffineTransform::~AffineTransform() {}

AffineTransform::AffineTransform()
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "()V"))), lang::Object(instance)
{}

AffineTransform::AffineTransform(AffineTransform& Tx)
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "(Ljava/awt/geom/AffineTransform;)V"), Tx.instance)), lang::Object(instance)
{}

AffineTransform::AffineTransform(double* flatmatrix, int flatmatrixc)
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "([D)V"), ToJDoubleArray(flatmatrix, flatmatrixc))), lang::Object(instance)
{}

AffineTransform::AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12)
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "(DDDDDD)V"), (jdouble)m00, (jdouble)m10, (jdouble)m01, (jdouble)m11, (jdouble)m02, (jdouble)m12)), lang::Object(instance)
{}

AffineTransform::AffineTransform(float* flatmatrix, int flatmatrixc)
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "([F)V"), ToJFloatArray(flatmatrix, flatmatrixc))), lang::Object(instance)
{}

AffineTransform::AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12)
 : instance(tc::jEnv->NewObject(InitAffineTransform(), tc::jEnv->GetMethodID(InitAffineTransform(), "<init>", "(FFFFFF)V"), (jfloat)m00, (jfloat)m10, (jfloat)m01, (jfloat)m11, (jfloat)m02, (jfloat)m12)), lang::Object(instance)
{}

lang::Object AffineTransform::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 0)));
}

void AffineTransform::Concatenate(AffineTransform Tx) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 1), Tx.instance);
}

AffineTransform AffineTransform::CreateInverse() {
   return (AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 2)));
}

Shape AffineTransform::CreateTransformedShape(Shape pSrc) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 3), pSrc.instance));
}

void AffineTransform::DeltaTransform(double* srcPts, int srcPtsc, int stcOff, double* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 4), ToJDoubleArray(srcPts, srcPtsc), (jint)stcOff, ToJDoubleArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

Point2D AffineTransform::DeltaTransform(Point2D ptSrc, Point2D ptDst) {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 5), ptSrc.instance, ptDst.instance));
}

bool AffineTransform::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAffineTransform, 6), obj.instance);
}

double AffineTransform::GetDeterminant() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 7));
}

void AffineTransform::GetMatrix(double* flatmatrix, int flatmatrixc) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 8), ToJDoubleArray(flatmatrix, flatmatrixc));
}

AffineTransform AffineTransform::GetQuadrantRotateInstance(int numquadrants) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[9], (jint)numquadrants));
}

AffineTransform AffineTransform::GetQuadrantRotateInstance(int numquadrants, double anchorx, double anchory) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[10], (jint)numquadrants, (jdouble)anchorx, (jdouble)anchory));
}

AffineTransform AffineTransform::GetRotateInstance(double theta) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[11], (jdouble)theta));
}

AffineTransform AffineTransform::GetRotateInstance(double vecx, double vecy) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[12], (jdouble)vecx, (jdouble)vecy));
}

AffineTransform AffineTransform::GetRotateInstance(double theta, double anchorx, double anchory) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[13], (jdouble)theta, (jdouble)anchorx, (jdouble)anchory));
}

AffineTransform AffineTransform::GetScaleInstance(double sx, double sy) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[14], (jdouble)sx, (jdouble)sy));
}

double AffineTransform::GetScaleX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 15));
}

double AffineTransform::GetScaleY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 16));
}

AffineTransform AffineTransform::GetShearInstance(double sdx, double shy) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[17], (jdouble)sdx, (jdouble)shy));
}

double AffineTransform::GetShearX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 18));
}

double AffineTransform::GetShearY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 19));
}

AffineTransform AffineTransform::GetTranslateInstance(double tx, double ty) {
   InitAffineTransform();
   return (AffineTransform)(tc::jEnv->CallStaticObjectMethod(clsAffineTransform.clazz, clsAffineTransform.methods[20], (jdouble)tx, (jdouble)ty));
}

double AffineTransform::GetTranslateX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 21));
}

double AffineTransform::GetTranslateY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsAffineTransform, 22));
}

int AffineTransform::GetType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAffineTransform, 23));
}

int AffineTransform::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAffineTransform, 24));
}

void AffineTransform::InverseTransform(double* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 25), ToJDoubleArray(srcPts, srcPtsc), (jint)srcOff, ToJDoubleArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

Point2D AffineTransform::InverseTransform(Point2D ptSrc, Point2D ptDst) {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 26), ptSrc.instance, ptDst.instance));
}

void AffineTransform::Invert() {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 27));
}

bool AffineTransform::IsIdentity() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAffineTransform, 28));
}

void AffineTransform::PreConcatenate(AffineTransform Tx) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 29), Tx.instance);
}

void AffineTransform::QuadrantRotate(int numquadrants) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 30), (jint)numquadrants);
}

void AffineTransform::QuadrantRotate(int numquadrants, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 31), (jint)numquadrants, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::Rotate(double theta) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 32), (jdouble)theta);
}

void AffineTransform::Rotate(double vecx, double vecy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 33), (jdouble)vecx, (jdouble)vecy);
}

void AffineTransform::Rotate(double theta, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 34), (jdouble)theta, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::Rotate(double vecx, double vecy, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 35), (jdouble)vecx, (jdouble)vecy, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::Scale(double sx, double sy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 36), (jdouble)sx, (jdouble)sy);
}

void AffineTransform::SetToIdentity() {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 37));
}

void AffineTransform::SetToQuadrantRotation(int numquadrants) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 38), (jint)numquadrants);
}

void AffineTransform::SetToQuadrantRotation(int numquadrants, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 39), (jint)numquadrants, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::SetToRotation(double theta) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 40), (jdouble)theta);
}

void AffineTransform::SetToRotation(double vecx, double vecy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 41), (jdouble)vecx, (jdouble)vecy);
}

void AffineTransform::SetToRotation(double theta, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 42), (jdouble)theta, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::SetToRotation(double vecx, double vecy, double anchorx, double anchory) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 43), (jdouble)vecx, (jdouble)vecy, (jdouble)anchorx, (jdouble)anchory);
}

void AffineTransform::SetToScale(double sx, double sy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 44), (jdouble)sx, (jdouble)sy);
}

void AffineTransform::SetToShear(double sdx, double shy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 45), (jdouble)sdx, (jdouble)shy);
}

void AffineTransform::SetToTranslation(double tx, double ty) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 46), (jdouble)tx, (jdouble)ty);
}

void AffineTransform::SetTransform(AffineTransform Tx) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 47), Tx.instance);
}

void AffineTransform::SetTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 48), (jdouble)m00, (jdouble)m10, (jdouble)m01, (jdouble)m11, (jdouble)m02, (jdouble)m12);
}

void AffineTransform::Shear(double shx, double shy) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 49), (jdouble)shx, (jdouble)shy);
}

char* AffineTransform::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 50)));
}

void AffineTransform::Transform(double* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 51), ToJDoubleArray(srcPts, srcPtsc), (jint)srcOff, ToJDoubleArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

void AffineTransform::Transform(double* srcPts, int srcPtsc, int srcOff, float* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 52), ToJDoubleArray(srcPts, srcPtsc), (jint)srcOff, ToJFloatArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

void AffineTransform::Transform(float* srcPts, int srcPtsc, int srcOff, double* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 53), ToJFloatArray(srcPts, srcPtsc), (jint)srcOff, ToJDoubleArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

void AffineTransform::Transform(float* srcPts, int srcPtsc, int srcOff, float* dstPts, int dstPtsc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 54), ToJFloatArray(srcPts, srcPtsc), (jint)srcOff, ToJFloatArray(dstPts, dstPtsc), (jint)dstOff, (jint)numPts);
}

void AffineTransform::Transform(Point2D* ptSrc, int ptSrcc, int srcOff, Point2D* ptDst, int ptDstc, int dstOff, int numPts) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 55), ToJObjectArray<Point2D>(ptSrc, ptSrcc), (jint)srcOff, ToJObjectArray<Point2D>(ptDst, ptDstc), (jint)dstOff, (jint)numPts);
}

Point2D AffineTransform::Transform(Point2D ptSrc, Point2D ptDst) {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsAffineTransform, 56), ptSrc.instance, ptDst.instance));
}

void AffineTransform::Translate(double tx, double ty) {
   tc::jEnv->CallVoidMethod(METHOD(clsAffineTransform, 57), (jdouble)tx, (jdouble)ty);
}


// java/awt/geom/PathIterator
// Name: PathIterator
// Base Class(es): [NativeObjectInstance]

NativeObject clsPathIterator;
jclass InitPathIterator() {
   return tcInitNativeObject(clsPathIterator, "java/awt/geom/PathIterator", { 
          { "currentSegment", "([D)I", false }, 
          { "currentSegment", "([F)I", false }, 
          { "getWindingRule", "()I", false }, 
          { "isDone", "()Z", false }, 
          { "next", "()V", false }
       }, { 
       });
}

PathIterator::PathIterator(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitPathIterator();
}

PathIterator::~PathIterator() {}

int PathIterator::CurrentSegment(double* coords, int coordsc) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPathIterator, 0), ToJDoubleArray(coords, coordsc));
}

int PathIterator::CurrentSegment(float* coords, int coordsc) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPathIterator, 1), ToJFloatArray(coords, coordsc));
}

int PathIterator::GetWindingRule() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPathIterator, 2));
}

bool PathIterator::IsDone() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPathIterator, 3));
}

void PathIterator::Next() {
   tc::jEnv->CallVoidMethod(METHOD(clsPathIterator, 4));
}


// java/awt/Shape
// Name: Shape
// Base Class(es): [NativeObjectInstance]

NativeObject clsShape;
jclass InitShape() {
   return tcInitNativeObject(clsShape, "java/awt/Shape", { 
          { "contains", "(DD)Z", false }, 
          { "contains", "(DDDD)Z", false }, 
          { "contains", "(Ljava/awt/geom/Point2D;)Z", false }, 
          { "contains", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;)Ljava/awt/geom/PathIterator;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;D)Ljava/awt/geom/PathIterator;", false }, 
          { "intersects", "(DDDD)Z", false }, 
          { "intersects", "(Ljava/awt/geom/Rectangle2D;)Z", false }
       }, { 
       });
}

Shape::Shape(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitShape();
}

Shape::~Shape() {}

bool Shape::Contains(double x, double y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 0), (jdouble)x, (jdouble)y);
}

bool Shape::Contains(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 1), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Shape::Contains(geom::Point2D p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 2), p.instance);
}

bool Shape::Contains(geom::Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 3), r.instance);
}

Rectangle Shape::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsShape, 4)));
}

geom::Rectangle2D Shape::GetBounds2D() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsShape, 5)));
}

geom::PathIterator Shape::GetPathIterator(geom::AffineTransform at) {
   return (geom::PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsShape, 6), at.instance));
}

geom::PathIterator Shape::GetPathIterator(geom::AffineTransform at, double flatness) {
   return (geom::PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsShape, 7), at.instance, (jdouble)flatness));
}

bool Shape::Intersects(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 8), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Shape::Intersects(geom::Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsShape, 9), r.instance);
}


// java/awt/geom/Line2D
// Name: Line2D
// Base Class(es): [Shape, lang::Object]

NativeObject clsLine2D;
jclass InitLine2D() {
   return tcInitNativeObject(clsLine2D, "java/awt/geom/Line2D", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "contains", "(DD)Z", false }, 
          { "contains", "(DDDD)Z", false }, 
          { "contains", "(Ljava/awt/geom/Point2D;)Z", false }, 
          { "contains", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getP1", "()Ljava/awt/geom/Point2D;", false }, 
          { "getP2", "()Ljava/awt/geom/Point2D;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;)Ljava/awt/geom/PathIterator;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;D)Ljava/awt/geom/PathIterator;", false }, 
          { "getX1", "()D", false }, 
          { "getX2", "()D", false }, 
          { "getY1", "()D", false }, 
          { "getY2", "()D", false }, 
          { "intersects", "(DDDD)Z", false }, 
          { "intersects", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "intersectsLine", "(DDDD)Z", false }, 
          { "intersectsLine", "(Ljava/awt/geom/Line2D;)Z", false }, 
          { "linesIntersect", "(DDDDDDDD)Z", true }, 
          { "ptLineDist", "(DD)D", false }, 
          { "ptLineDist", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "ptLineDistSq", "(DD)D", false }, 
          { "ptLineDistSq", "(DDDDDD)D", true }, 
          { "ptLineDistSq", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "ptSegDist", "(DD)D", false }, 
          { "ptSegDist", "(DDDDDD)D", true }, 
          { "ptSegDist", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "ptSegDistSq", "(DD)D", false }, 
          { "ptSegDistSq", "(DDDDDD)D", true }, 
          { "ptSegDistSq", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "relativeCCW", "(DD)I", false }, 
          { "relativeCCW", "(DDDDDD)I", true }, 
          { "relativeCCW", "(Ljava/awt/geom/Point2D;)I", false }, 
          { "setLine", "(DDDD)V", false }, 
          { "setLine", "(Ljava/awt/geom/Line2D;)V", false }, 
          { "setLine", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)V", false }
       }, { 
       });
}

Line2D::Line2D(jobject instance)
 : instance(instance), Shape(instance), lang::Object(instance) {
    InitLine2D();
}

Line2D::~Line2D() {}

lang::Object Line2D::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 0)));
}

bool Line2D::Contains(double x, double y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 1), (jdouble)x, (jdouble)y);
}

bool Line2D::Contains(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 2), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Line2D::Contains(Point2D p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 3), p.instance);
}

bool Line2D::Contains(Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 4), r.instance);
}

Rectangle Line2D::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 5)));
}

Point2D Line2D::GetP1() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 6)));
}

Point2D Line2D::GetP2() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 7)));
}

PathIterator Line2D::GetPathIterator(AffineTransform at) {
   return (PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 8), at.instance));
}

PathIterator Line2D::GetPathIterator(AffineTransform at, double flatness) {
   return (PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D, 9), at.instance, (jdouble)flatness));
}

double Line2D::GetX1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 10));
}

double Line2D::GetX2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 11));
}

double Line2D::GetY1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 12));
}

double Line2D::GetY2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 13));
}

bool Line2D::Intersects(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 14), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Line2D::Intersects(Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 15), r.instance);
}

bool Line2D::IntersectsLine(double x1, double y1, double x2, double y2) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 16), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

bool Line2D::IntersectsLine(Line2D l) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsLine2D, 17), l.instance);
}

bool Line2D::LinesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
   InitLine2D();
   return (bool)tc::jEnv->CallStaticBooleanMethod(clsLine2D.clazz, clsLine2D.methods[18], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2, (jdouble)x3, (jdouble)y3, (jdouble)x4, (jdouble)y4);
}

double Line2D::PtLineDist(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 19), (jdouble)px, (jdouble)py);
}

double Line2D::PtLineDist(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 20), pt.instance);
}

double Line2D::PtLineDistSq(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 21), (jdouble)px, (jdouble)py);
}

double Line2D::PtLineDistSq(double x1, double y1, double x2, double y2, double px, double py) {
   InitLine2D();
   return (double)tc::jEnv->CallStaticDoubleMethod(clsLine2D.clazz, clsLine2D.methods[22], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2, (jdouble)px, (jdouble)py);
}

double Line2D::ptLineDistSq(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 23), pt.instance);
}

double Line2D::PtSegDist(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 24), (jdouble)px, (jdouble)py);
}

double Line2D::PtSegDist(double x1, double y1, double x2, double y2, double px, double py) {
   InitLine2D();
   return (double)tc::jEnv->CallStaticDoubleMethod(clsLine2D.clazz, clsLine2D.methods[25], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2, (jdouble)px, (jdouble)py);
}

double Line2D::PtSegDist(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 26), pt.instance);
}

double Line2D::ptSegDistSq(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 27), (jdouble)px, (jdouble)py);
}

double Line2D::PtSegDistSq(double x1, double y1, double x2, double y2, double px, double py) {
   InitLine2D();
   return (double)tc::jEnv->CallStaticDoubleMethod(clsLine2D.clazz, clsLine2D.methods[28], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2, (jdouble)px, (jdouble)py);
}

double Line2D::PtSegDistSq(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D, 29), pt.instance);
}

int Line2D::RelativeCCW(double px, double py) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsLine2D, 30), (jdouble)px, (jdouble)py);
}

int Line2D::RelativeCCW(double x1, double y1, double x2, double y2, double px, double py) {
   InitLine2D();
   return (int)tc::jEnv->CallStaticIntMethod(clsLine2D.clazz, clsLine2D.methods[31], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2, (jdouble)px, (jdouble)py);
}

int Line2D::RelativeCCW(Point2D p) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsLine2D, 32), p.instance);
}

void Line2D::SetLine(double x1, double y1, double x2, double y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D, 33), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

void Line2D::SetLine(Line2D l) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D, 34), l.instance);
}

void Line2D::SetLine(Point2D p1, Point2D p2) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D, 35), p1.instance, p2.instance);
}


// java/awt/geom/Line2D/Double
// Name: Line2D::Double
// Base Class(es): [Line2D]

NativeObject clsLine2D_Double;
jclass InitLine2D_Double() {
   return tcInitNativeObject(clsLine2D_Double, "java/awt/geom/Line2D/Double", { 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getP1", "()Ljava/awt/geom/Point2D;", false }, 
          { "getP2", "()Ljava/awt/geom/Point2D;", false }, 
          { "getX1", "()D", false }, 
          { "getX2", "()D", false }, 
          { "getY1", "()D", false }, 
          { "getY2", "()D", false }, 
          { "setLine", "(DDDD)V", false }
       }, { 
       });
}

Line2D::Double::Double(jobject instance)
 : instance(instance), Line2D(instance) {
    InitLine2D_Double();
}

Line2D::Double::~Double() {}

Line2D::Double::Double()
 : instance(tc::jEnv->NewObject(InitLine2D_Double(), tc::jEnv->GetMethodID(InitLine2D_Double(), "<init>", "()V"))), Line2D(instance)
{}

Line2D::Double::Double(double x1, double y1, double x2, double y2)
 : instance(tc::jEnv->NewObject(InitLine2D_Double(), tc::jEnv->GetMethodID(InitLine2D_Double(), "<init>", "(DDDD)V"), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2)), Line2D(instance)
{}

Line2D::Double::Double(Point2D p1, Point2D p2)
 : instance(tc::jEnv->NewObject(InitLine2D_Double(), tc::jEnv->GetMethodID(InitLine2D_Double(), "<init>", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)V"), p1.instance, p2.instance)), Line2D(instance)
{}

Rectangle2D Line2D::Double::GetBounds2D() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Double, 0)));
}

Point2D Line2D::Double::GetP1() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Double, 1)));
}

Point2D Line2D::Double::GetP2() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Double, 2)));
}

double Line2D::Double::GetX1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Double, 3));
}

double Line2D::Double::GetX2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Double, 4));
}

double Line2D::Double::GetY1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Double, 5));
}

double Line2D::Double::GetY2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Double, 6));
}

void Line2D::Double::SetLine(double x1, double y1, double x2, double y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D_Double, 7), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}


// java/awt/geom/Line2D/Float
// Name: Line2D::Float
// Base Class(es): [Line2D]

NativeObject clsLine2D_Float;
jclass InitLine2D_Float() {
   return tcInitNativeObject(clsLine2D_Float, "java/awt/geom/Line2D/Float", { 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getP1", "()Ljava/awt/geom/Point2D;", false }, 
          { "getP2", "()Ljava/awt/geom/Point2D;", false }, 
          { "getX1", "()D", false }, 
          { "getX2", "()D", false }, 
          { "getY1", "()D", false }, 
          { "getY2", "()D", false }, 
          { "setLine", "(DDDD)V", false }, 
          { "setLine", "(FFFF)V", false }
       }, { 
       });
}

Line2D::Float::Float(jobject instance)
 : instance(instance), Line2D(instance) {
    InitLine2D_Float();
}

Line2D::Float::~Float() {}

Line2D::Float::Float()
 : instance(tc::jEnv->NewObject(InitLine2D_Float(), tc::jEnv->GetMethodID(InitLine2D_Float(), "<init>", "()V"))), Line2D(instance)
{}

Line2D::Float::Float(float x1, float y1, float x2, float y2)
 : instance(tc::jEnv->NewObject(InitLine2D_Float(), tc::jEnv->GetMethodID(InitLine2D_Float(), "<init>", "(FFFF)V"), (jfloat)x1, (jfloat)y1, (jfloat)x2, (jfloat)y2)), Line2D(instance)
{}

Line2D::Float::Float(Point2D p1, Point2D p2)
 : instance(tc::jEnv->NewObject(InitLine2D_Float(), tc::jEnv->GetMethodID(InitLine2D_Float(), "<init>", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)V"), p1.instance, p2.instance)), Line2D(instance)
{}

Rectangle2D Line2D::Float::GetBounds2D() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Float, 0)));
}

Point2D Line2D::Float::GetP1() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Float, 1)));
}

Point2D Line2D::Float::GetP2() {
   return (Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsLine2D_Float, 2)));
}

double Line2D::Float::GetX1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Float, 3));
}

double Line2D::Float::GetX2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Float, 4));
}

double Line2D::Float::GetY1() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Float, 5));
}

double Line2D::Float::GetY2() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsLine2D_Float, 6));
}

void Line2D::Float::SetLine(double x1, double y1, double x2, double y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D_Float, 7), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

void Line2D::Float::SetLine(float x1, float y1, float x2, float y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsLine2D_Float, 8), (jfloat)x1, (jfloat)y1, (jfloat)x2, (jfloat)y2);
}


// java/awt/geom/RectangularShape
// Name: RectangularShape
// Base Class(es): [Shape, lang::Object]

NativeObject clsRectangularShape;
jclass InitRectangularShape() {
   return tcInitNativeObject(clsRectangularShape, "java/awt/geom/RectangularShape", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "contains", "(Ljava/awt/geom/Point2D;)Z", false }, 
          { "contains", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getCenterX", "()D", false }, 
          { "getCenterY", "()D", false }, 
          { "getFrame", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getHeight", "()D", false }, 
          { "getMaxX", "()D", false }, 
          { "getMaxY", "()D", false }, 
          { "getMinX", "()D", false }, 
          { "getMinY", "()D", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;D)Ljava/awt/geom/PathIterator;", false }, 
          { "getWidth", "()D", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "intersects", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "isEmpty", "()Z", false }, 
          { "setFrame", "(DDDD)V", false }, 
          { "setFrame", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Dimension2D;)V", false }, 
          { "setFrame", "(Ljava/awt/geom/Rectangle2D;)V", false }, 
          { "setFrameFromCenter", "(DDDD)V", false }, 
          { "setFrameFromCenter", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)V", false }, 
          { "setFrameFromDiagonal", "(DDDD)V", false }, 
          { "setFrameFromDiagonal", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)V", false }
       }, { 
       });
}

RectangularShape::RectangularShape(jobject instance)
 : instance(instance), Shape(instance), lang::Object(instance) {
    InitRectangularShape();
}

RectangularShape::~RectangularShape() {}

lang::Object RectangularShape::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRectangularShape, 0)));
}

bool RectangularShape::Contains(Point2D p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangularShape, 1), p.instance);
}

bool RectangularShape::Contains(Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangularShape, 2), r.instance);
}

Rectangle RectangularShape::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsRectangularShape, 3)));
}

double RectangularShape::GetCenterX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 4));
}

double RectangularShape::GetCenterY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 5));
}

Rectangle2D RectangularShape::GetFrame() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangularShape, 6)));
}

double RectangularShape::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 7));
}

double RectangularShape::GetMaxX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 8));
}

double RectangularShape::GetMaxY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 9));
}

double RectangularShape::GetMinX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 10));
}

double RectangularShape::GetMinY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 11));
}

PathIterator RectangularShape::GetPathIterator(AffineTransform at, double flatness) {
   return (PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsRectangularShape, 12), at.instance, (jdouble)flatness));
}

double RectangularShape::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 13));
}

double RectangularShape::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 14));
}

double RectangularShape::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangularShape, 15));
}

bool RectangularShape::Intersects(Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangularShape, 16), r.instance);
}

bool RectangularShape::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangularShape, 17));
}

void RectangularShape::SetFrame(double x, double y, double w, double h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 18), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

void RectangularShape::SetFrame(Point2D loc, Dimension2D size) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 19), loc.instance, size.instance);
}

void RectangularShape::SetFrame(Rectangle2D r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 20), r.instance);
}

void RectangularShape::SetFrameFromCenter(double centerX, double centerY, double cornerX, double cronerY) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 21), (jdouble)centerX, (jdouble)centerY, (jdouble)cornerX, (jdouble)cronerY);
}

void RectangularShape::SetFrameFromCenter(Point2D center, Point2D corner) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 22), center.instance, corner.instance);
}

void RectangularShape::SetFrameFromDiagonal(double x1, double y1, double x2, double y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 23), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

void RectangularShape::SetFrameFromDiagonal(Point2D p1, Point2D p2) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangularShape, 24), p1.instance, p2.instance);
}


// java/awt/geom/Rectangle2D
// Name: Rectangle2D
// Base Class(es): [RectangularShape]

NativeObject clsRectangle2D;
jclass InitRectangle2D() {
   return tcInitNativeObject(clsRectangle2D, "java/awt/geom/Rectangle2D", { 
          { "add", "(DD)V", false }, 
          { "add", "(Ljava/awt/geom/Point2D;)V", false }, 
          { "add", "(Ljava/awt/geom/Rectangle2D;)V", false }, 
          { "contains", "(DD)Z", false }, 
          { "contains", "(DDDD)Z", false }, 
          { "createIntersection", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "createUnion", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;)Ljava/awt/geom/PathIterator;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;D)Ljava/awt/geom/PathIterator;", false }, 
          { "hashCode", "()I", false }, 
          { "intersect", "(Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/Rectangle2D;)V", true }, 
          { "intersects", "(DDDD)Z", false }, 
          { "intersectsLine", "(DDDD)Z", false }, 
          { "intersectsLine", "(Ljava/awt/geom/Line2D;)Z", false }, 
          { "outcode", "(DD)I", false }, 
          { "outcode", "(Ljava/awt/geom/Point2D;)I", false }, 
          { "setFrame", "(DDDD)V", false }, 
          { "setRect", "(DDDD)V", false }, 
          { "setRect", "(Ljava/awt/geom/Rectangle2D;)V", false }, 
          { "union", "(Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/Rectangle2D;)V", false }
       }, { 
       });
}

Rectangle2D::Rectangle2D(jobject instance)
 : instance(instance), RectangularShape(instance) {
    InitRectangle2D();
}

Rectangle2D::~Rectangle2D() {}

void Rectangle2D::Add(double newx, double newy) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 0), (jdouble)newx, (jdouble)newy);
}

void Rectangle2D::Add(Point2D pt) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 1), pt.instance);
}

void Rectangle2D::Add(Rectangle2D r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 2), r.instance);
}

bool Rectangle2D::Contains(double x, double y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 3), (jdouble)x, (jdouble)y);
}

bool Rectangle2D::Contains(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 4), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

Rectangle2D Rectangle2D::CreateIntersection(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D, 5), r.instance));
}

Rectangle2D Rectangle2D::CreateUnion(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D, 6), r.instance));
}

bool Rectangle2D::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 7), obj.instance);
}

Rectangle2D Rectangle2D::GetBounds2D() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D, 8)));
}

PathIterator Rectangle2D::GetPathIterator(AffineTransform at) {
   return (PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D, 9), at.instance));
}

PathIterator Rectangle2D::GetPathIterator(AffineTransform at, double flatness) {
   return (PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D, 10), at.instance, (jdouble)flatness));
}

int Rectangle2D::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle2D, 11));
}

void Rectangle2D::Intersect(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest) {
   InitRectangle2D();
   tc::jEnv->CallStaticVoidMethod(clsRectangle2D.clazz, clsRectangle2D.methods[12], src1.instance, src2.instance, dest.instance);
}

bool Rectangle2D::Intersects(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 13), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Rectangle2D::IntersectsLine(double x1, double y1, double x2, double y2) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 14), (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

bool Rectangle2D::IntersectsLine(Line2D l) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D, 15), l.instance);
}

int Rectangle2D::Outcode(double x, double y) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle2D, 16), (jdouble)x, (jdouble)y);
}

int Rectangle2D::Outcode(Point2D p) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle2D, 17), p.instance);
}

void Rectangle2D::SetFrame(double x, double y, double w, double h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 18), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

void Rectangle2D::SetRect(double x, double y, double w, double h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 19), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

void Rectangle2D::SetRect(Rectangle2D r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 20), r.instance);
}

void Rectangle2D::Union(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D, 21), src1.instance, src2.instance, dest.instance);
}


// java/awt/geom/Rectangle2D/Double
// Name: Rectangle2D::Double
// Base Class(es): [Rectangle2D]

NativeObject clsRectangle2D_Double;
jclass InitRectangle2D_Double() {
   return tcInitNativeObject(clsRectangle2D_Double, "java/awt/geom/Rectangle2D/Double", { 
          { "createIntersection", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "createUnion", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getHeight", "()D", false }, 
          { "getWidth", "()D", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "isEmpty", "()Z", false }, 
          { "outcode", "(DD)I", false }, 
          { "setRect", "(DDDD)V", false }, 
          { "setRect", "(Ljava/awt/geom/Rectangle2D;)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Rectangle2D::Double::Double(jobject instance)
 : instance(instance), Rectangle2D(instance) {
    InitRectangle2D_Double();
}

Rectangle2D::Double::~Double() {}

Rectangle2D::Double::Double()
 : instance(tc::jEnv->NewObject(InitRectangle2D_Double(), tc::jEnv->GetMethodID(InitRectangle2D_Double(), "<init>", "()V"))), Rectangle2D(instance)
{}

Rectangle2D::Double::Double(double x, double y, double w, double h)
 : instance(tc::jEnv->NewObject(InitRectangle2D_Double(), tc::jEnv->GetMethodID(InitRectangle2D_Double(), "<init>", "(DDDD)V"), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h)), Rectangle2D(instance)
{}

Rectangle2D Rectangle2D::Double::CreateIntersection(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Double, 0), r.instance));
}

Rectangle2D Rectangle2D::Double::CreateUnion(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Double, 1), r.instance));
}

Rectangle2D Rectangle2D::Double::GetBounds2D() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Double, 2)));
}

double Rectangle2D::Double::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Double, 3));
}

double Rectangle2D::Double::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Double, 4));
}

double Rectangle2D::Double::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Double, 5));
}

double Rectangle2D::Double::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Double, 6));
}

bool Rectangle2D::Double::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D_Double, 7));
}

int Rectangle2D::Double::Outcode(double x, double y) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle2D_Double, 8), (jdouble)x, (jdouble)y);
}

void Rectangle2D::Double::SetRect(double x, double y, double w, double h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D_Double, 9), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

void Rectangle2D::Double::SetRect(Rectangle2D r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D_Double, 10), r.instance);
}

char* Rectangle2D::Double::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Double, 11)));
}


// java/awt/geom/Rectangle2D/Float
// Name: Rectangle2D::Float
// Base Class(es): [Rectangle2D]

NativeObject clsRectangle2D_Float;
jclass InitRectangle2D_Float() {
   return tcInitNativeObject(clsRectangle2D_Float, "java/awt/geom/Rectangle2D/Float", { 
          { "createIntersection", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "createUnion", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getHeight", "()D", false }, 
          { "getWidth", "()D", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "isEmpty", "()Z", false }, 
          { "outcode", "(DD)I", false }, 
          { "setRect", "(DDDD)V", false }, 
          { "setRect", "(FFFF)V", false }, 
          { "setRect", "(Ljava/awt/geom/Rectangle2D;)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Rectangle2D::Float::Float(jobject instance)
 : instance(instance), Rectangle2D(instance) {
    InitRectangle2D_Float();
}

Rectangle2D::Float::~Float() {}

Rectangle2D::Float::Float()
 : instance(tc::jEnv->NewObject(InitRectangle2D_Float(), tc::jEnv->GetMethodID(InitRectangle2D_Float(), "<init>", "()V"))), Rectangle2D(instance)
{}

Rectangle2D::Float::Float(float x, float y, float w, float h)
 : instance(tc::jEnv->NewObject(InitRectangle2D_Float(), tc::jEnv->GetMethodID(InitRectangle2D_Float(), "<init>", "(FFFF)V"), (jfloat)x, (jfloat)y, (jfloat)w, (jfloat)h)), Rectangle2D(instance)
{}

Rectangle2D Rectangle2D::Float::CreateIntersection(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Float, 0), r.instance));
}

Rectangle2D Rectangle2D::Float::CreateUnion(Rectangle2D r) {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Float, 1), r.instance));
}

Rectangle2D Rectangle2D::Float::GetBounds2D() {
   return (Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Float, 2)));
}

double Rectangle2D::Float::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Float, 3));
}

double Rectangle2D::Float::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Float, 4));
}

double Rectangle2D::Float::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Float, 5));
}

double Rectangle2D::Float::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle2D_Float, 6));
}

bool Rectangle2D::Float::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle2D_Float, 7));
}

int Rectangle2D::Float::Outcode(double x, double y) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle2D_Float, 8), (jdouble)x, (jdouble)y);
}

void Rectangle2D::Float::SetRect(double x, double y, double w, double h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D_Float, 9), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

void Rectangle2D::Float::SetRect(float x, float y, float w, float h) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D_Float, 10), (jfloat)x, (jfloat)y, (jfloat)w, (jfloat)h);
}

void Rectangle2D::Float::SetRect(Rectangle2D r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle2D_Float, 11), r.instance);
}

char* Rectangle2D::Float::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsRectangle2D_Float, 12)));
}


// java/awt/Rectangle
// Name: Rectangle
// Base Class(es): [geom::Rectangle2D]

NativeObject clsRectangle;
jclass InitRectangle() {
   return tcInitNativeObject(clsRectangle, "java/awt/Rectangle", { 
          { "add", "(II)V", false }, 
          { "add", "(Ljava/awt/Point;)V", false }, 
          { "add", "(Ljava/awt/Rectangle;)V", false }, 
          { "contains", "(II)Z", false }, 
          { "contains", "(IIII)Z", false }, 
          { "contains", "(Ljava/awt/Point;)Z", false }, 
          { "contains", "(Ljava/awt/Rectangle;)Z", false }, 
          { "createIntersection", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "createUnion", "(Ljava/awt/geom/Rectangle2D;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getHeight", "()D", false }, 
          { "getLocation", "()Ljava/awt/Point;", false }, 
          { "getSize", "()Ljava/awt/Dimension;", false }, 
          { "getWidth", "()D", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "grow", "(II)V", false }, 
          { "intersection", "(Ljava/awt/Rectangle;)Ljava/awt/Rectangle;", false }, 
          { "intersects", "(Ljava/awt/Rectangle;)Z", false }, 
          { "isEmpty", "()Z", false }, 
          { "outcode", "(DD)I", false }, 
          { "setBounds", "(IIII)V", false }, 
          { "setBounds", "(Ljava/awt/Rectangle;)V", false }, 
          { "setLocation", "(II)V", false }, 
          { "setLocation", "(Ljava/awt/Point;)V", false }, 
          { "setRect", "(DDDD)V", false }, 
          { "setSize", "(Ljava/awt/Dimension;)V", false }, 
          { "setSize", "(II)V", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "translate", "(II)V", false }, 
          { "union", "(Ljava/awt/Rectangle;)Ljava/awt/Rectangle;", false }
       }, { 
       });
}

Rectangle::Rectangle(jobject instance)
 : instance(instance), geom::Rectangle2D(instance) {
    InitRectangle();
}

Rectangle::~Rectangle() {}

Rectangle::Rectangle()
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "()V"))), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(Dimension d)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(Ljava/awt/Dimension;)V"), d.instance)), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(int width, int height)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(II)V"), (jint)width, (jint)height)), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(int x, int y, int width, int height)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(IIII)V"), (jint)x, (jint)y, (jint)width, (jint)height)), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(Point p)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(Ljava/awt/Point;)V"), p.instance)), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(Point p, Dimension d)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(Ljava/awt/Point;Ljava/awt/Dimension;)V"), p.instance, d.instance)), geom::Rectangle2D(instance)
{}

Rectangle::Rectangle(Rectangle& r)
 : instance(tc::jEnv->NewObject(InitRectangle(), tc::jEnv->GetMethodID(InitRectangle(), "<init>", "(Ljava/awt/Rectangle;)V"), r.instance)), geom::Rectangle2D(instance)
{}

void Rectangle::Add(int newx, int newy) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 0), (jint)newx, (jint)newy);
}

void Rectangle::Add(Point pt) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 1), pt.instance);
}

void Rectangle::Add(Rectangle r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 2), r.instance);
}

bool Rectangle::Contains(int x, int y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 3), (jint)x, (jint)y);
}

bool Rectangle::Contains(int X, int Y, int W, int H) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 4), (jint)X, (jint)Y, (jint)W, (jint)H);
}

bool Rectangle::Contains(Point p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 5), p.instance);
}

bool Rectangle::Contains(Rectangle r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 6), r.instance);
}

geom::Rectangle2D Rectangle::CreateIntersection(geom::Rectangle2D r) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 7), r.instance));
}

geom::Rectangle2D Rectangle::CreateUnion(geom::Rectangle2D r) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 8), r.instance));
}

bool Rectangle::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 9), obj.instance);
}

Rectangle Rectangle::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 10)));
}

geom::Rectangle2D Rectangle::GetBounds2D() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 11)));
}

double Rectangle::GetHeight() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle, 12));
}

Point Rectangle::GetLocation() {
   return (Point)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 13)));
}

Dimension Rectangle::GetSize() {
   return (Dimension)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 14)));
}

double Rectangle::GetWidth() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle, 15));
}

double Rectangle::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle, 16));
}

double Rectangle::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRectangle, 17));
}

void Rectangle::Grow(int h, int v) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 18), (jint)h, (jint)v);
}

Rectangle Rectangle::Intersection(Rectangle r) {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 19), r.instance));
}

bool Rectangle::Intersects(Rectangle r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 20), r.instance);
}

bool Rectangle::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRectangle, 21));
}

int Rectangle::Outcode(double x, double y) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRectangle, 22), (jdouble)x, (jdouble)y);
}

void Rectangle::SetBounds(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 23), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Rectangle::SetBounds(Rectangle r) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 24), r.instance);
}

void Rectangle::SetLocation(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 25), (jint)x, (jint)y);
}

void Rectangle::SetLocation(Point p) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 26), p.instance);
}

void Rectangle::SetRect(double x, double y, double width, double height) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 27), (jdouble)x, (jdouble)y, (jdouble)width, (jdouble)height);
}

void Rectangle::SetSize(Dimension d) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 28), d.instance);
}

void Rectangle::SetSize(int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 29), (jint)width, (jint)height);
}

char* Rectangle::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 30)));
}

void Rectangle::Translate(int dx, int dy) {
   tc::jEnv->CallVoidMethod(METHOD(clsRectangle, 31), (jint)dx, (jint)dy);
}

Rectangle Rectangle::Union(Rectangle r) {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsRectangle, 32), r.instance));
}


// java/awt/geom/Point2D
// Name: Point2D
// Base Class(es): [lang::Object]

NativeObject clsPoint2D;
jclass InitPoint2D() {
   return tcInitNativeObject(clsPoint2D, "java/awt/geom/Point2D", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "distance", "(DD)D", false }, 
          { "distance", "(DDDD)D", true }, 
          { "distance", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "distanceSq", "(DD)D", false }, 
          { "distanceSq", "(DDDD)D", true }, 
          { "distanceSq", "(Ljava/awt/geom/Point2D;)D", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "hashCode", "()I", false }, 
          { "setLocation", "(DD)V", false }, 
          { "setLocation", "(Ljava/awt/geom/Point2D;)V", false }
       }, { 
       });
}

Point2D::Point2D(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitPoint2D();
}

Point2D::~Point2D() {}

Point2D::Point2D()
 : instance(tc::jEnv->NewObject(InitPoint2D(), tc::jEnv->GetMethodID(InitPoint2D(), "<init>", "()V"))), lang::Object(instance)
{}

lang::Object Point2D::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsPoint2D, 0)));
}

double Point2D::Distance(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 1), (jdouble)px, (jdouble)py);
}

double Point2D::Distance(double x1, double y1, double x2, double y2) {
   InitPoint2D();
   return (double)tc::jEnv->CallStaticDoubleMethod(clsPoint2D.clazz, clsPoint2D.methods[2], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

double Point2D::Distance(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 3), pt.instance);
}

double Point2D::DistanceSq(double px, double py) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 4), (jdouble)px, (jdouble)py);
}

double Point2D::DistanceSq(double x1, double y1, double x2, double y2) {
   InitPoint2D();
   return (double)tc::jEnv->CallStaticDoubleMethod(clsPoint2D.clazz, clsPoint2D.methods[5], (jdouble)x1, (jdouble)y1, (jdouble)x2, (jdouble)y2);
}

double Point2D::DistanceSq(Point2D pt) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 6), pt.instance);
}

bool Point2D::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPoint2D, 7), obj.instance);
}

double Point2D::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 8));
}

double Point2D::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D, 9));
}

int Point2D::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPoint2D, 10));
}

void Point2D::SetLocation(double x, double y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint2D, 11), (jdouble)x, (jdouble)y);
}

void Point2D::SetLocation(Point2D p) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint2D, 12), p.instance);
}


// java/awt/geom/Point2D/Double
// Name: Point2D::Double
// Base Class(es): [Point2D]

NativeObject clsPoint2D_Double;
jclass InitPoint2D_Double() {
   return tcInitNativeObject(clsPoint2D_Double, "java/awt/geom/Point2D/Double", { 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "setLocation", "(DD)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Point2D::Double::Double(jobject instance)
 : instance(instance), Point2D(instance) {
    InitPoint2D_Double();
}

Point2D::Double::~Double() {}

Point2D::Double::Double()
 : instance(tc::jEnv->NewObject(InitPoint2D_Double(), tc::jEnv->GetMethodID(InitPoint2D_Double(), "<init>", "()V"))), Point2D(instance)
{}

Point2D::Double::Double(double x, double y)
 : instance(tc::jEnv->NewObject(InitPoint2D_Double(), tc::jEnv->GetMethodID(InitPoint2D_Double(), "<init>", "(DD)V"), (jdouble)x, (jdouble)y)), Point2D(instance)
{}

double Point2D::Double::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D_Double, 0));
}

double Point2D::Double::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D_Double, 1));
}

void Point2D::Double::SetLocation(double x, double y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint2D_Double, 2), (jdouble)x, (jdouble)y);
}

char* Point2D::Double::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPoint2D_Double, 3)));
}


// java/awt/geom/Point2D/Float
// Name: Point2D::Float
// Base Class(es): [Point2D]

NativeObject clsPoint2D_Float;
jclass InitPoint2D_Float() {
   return tcInitNativeObject(clsPoint2D_Float, "java/awt/geom/Point2D/Float", { 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "setLocation", "(DD)V", false }, 
          { "setLocation", "(FF)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Point2D::Float::Float(jobject instance)
 : instance(instance), Point2D(instance) {
    InitPoint2D_Float();
}

Point2D::Float::~Float() {}

Point2D::Float::Float()
 : instance(tc::jEnv->NewObject(InitPoint2D_Float(), tc::jEnv->GetMethodID(InitPoint2D_Float(), "<init>", "()V"))), Point2D(instance)
{}

Point2D::Float::Float(float x, float y)
 : instance(tc::jEnv->NewObject(InitPoint2D_Float(), tc::jEnv->GetMethodID(InitPoint2D_Float(), "<init>", "(FF)V"), (jfloat)x, (jfloat)y)), Point2D(instance)
{}

double Point2D::Float::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D_Float, 0));
}

double Point2D::Float::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint2D_Float, 1));
}

void Point2D::Float::SetLocation(double x, double y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint2D_Float, 2), (jdouble)x, (jdouble)y);
}

void Point2D::Float::SetLocation(float x, float y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint2D_Float, 3), (jfloat)x, (jfloat)y);
}

char* Point2D::Float::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPoint2D_Float, 4)));
}


// java/awt/Point
// Name: Point
// Base Class(es): [geom::Point2D]

NativeObject clsPoint;
jclass InitPoint() {
   return tcInitNativeObject(clsPoint, "java/awt/Point", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getLocation", "()Ljava/awt/Point;", false }, 
          { "getX", "()D", false }, 
          { "getY", "()D", false }, 
          { "move", "(II)V", false }, 
          { "setLocation", "(DD)V", false }, 
          { "setLocation", "(II)V", false }, 
          { "setLocation", "(Ljava/awt/Point;)V", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "translate", "(II)V", false }
       }, { 
       });
}

Point::Point(jobject instance)
 : instance(instance), geom::Point2D(instance) {
    InitPoint();
}

Point::~Point() {}

Point::Point()
 : instance(tc::jEnv->NewObject(InitPoint(), tc::jEnv->GetMethodID(InitPoint(), "<init>", "()V"))), geom::Point2D(instance)
{}

Point::Point(int x, int y)
 : instance(tc::jEnv->NewObject(InitPoint(), tc::jEnv->GetMethodID(InitPoint(), "<init>", "(II)V"), (jint)x, (jint)y)), geom::Point2D(instance)
{}

Point::Point(Point& p)
 : instance(tc::jEnv->NewObject(InitPoint(), tc::jEnv->GetMethodID(InitPoint(), "<init>", "(Ljava/awt/Point;)V"), p.instance)), geom::Point2D(instance)
{}

bool Point::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPoint, 0), obj.instance);
}

Point Point::GetLocation() {
   return (Point)(tc::jEnv->CallObjectMethod(METHOD(clsPoint, 1)));
}

double Point::GetX() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint, 2));
}

double Point::GetY() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsPoint, 3));
}

void Point::Move(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint, 4), (jint)x, (jint)y);
}

void Point::SetLocation(double x, double y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint, 5), (jdouble)x, (jdouble)y);
}

void Point::SetLocation(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint, 6), (jint)x, (jint)y);
}

void Point::SetLocation(Point p) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint, 7), p.instance);
}

char* Point::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPoint, 8)));
}

void Point::Translate(int dx, int dy) {
   tc::jEnv->CallVoidMethod(METHOD(clsPoint, 9), (jint)dx, (jint)dy);
}


// java/awt/Polygon
// Name: Polygon
// Base Class(es): [Shape, lang::Object]

NativeObject clsPolygon;
jclass InitPolygon() {
   return tcInitNativeObject(clsPolygon, "java/awt/Polygon", { 
          { "addPoint", "(II)V", false }, 
          { "contains", "(DD)Z", false }, 
          { "contains", "(DDDD)Z", false }, 
          { "contains", "(II)Z", false }, 
          { "contains", "(Ljava/awt/Point;)Z", false }, 
          { "contains", "(Ljava/awt/geom/Point2D;)Z", false }, 
          { "contains", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;)Ljava/awt/geom/PathIterator;", false }, 
          { "getPathIterator", "(Ljava/awt/geom/AffineTransform;D)Ljava/awt/geom/PathIterator;", false }, 
          { "intersects", "(DDDD)Z", false }, 
          { "intersects", "(Ljava/awt/geom/Rectangle2D;)Z", false }, 
          { "invalidate", "()V", false }, 
          { "reset", "()V", false }, 
          { "translate", "(II)V", false }
       }, { 
       });
}

Polygon::Polygon(jobject instance)
 : instance(instance), Shape(instance), lang::Object(instance) {
    InitPolygon();
}

Polygon::~Polygon() {}

Polygon::Polygon()
 : instance(tc::jEnv->NewObject(InitPolygon(), tc::jEnv->GetMethodID(InitPolygon(), "<init>", "()V"))), Shape(instance), lang::Object(instance)
{}

Polygon::Polygon(int* xpoints, int xpointsc, int* ypoints, int ypointsc, int npoints)
 : instance(tc::jEnv->NewObject(InitPolygon(), tc::jEnv->GetMethodID(InitPolygon(), "<init>", "([I[II)V"), ToJIntArray(xpoints, xpointsc), ToJIntArray(ypoints, ypointsc), (jint)npoints)), Shape(instance), lang::Object(instance)
{}

void Polygon::AddPoint(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsPolygon, 0), (jint)x, (jint)y);
}

bool Polygon::Contains(double x, double y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 1), (jdouble)x, (jdouble)y);
}

bool Polygon::Contains(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 2), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Polygon::Contains(int x, int y) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 3), (jint)x, (jint)y);
}

bool Polygon::Contains(Point p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 4), p.instance);
}

bool Polygon::Contains(geom::Point2D p) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 5), p.instance);
}

bool Polygon::Contains(geom::Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 6), r.instance);
}

Rectangle Polygon::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsPolygon, 7)));
}

geom::Rectangle2D Polygon::GetBounds2D() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsPolygon, 8)));
}

geom::PathIterator Polygon::GetPathIterator(geom::AffineTransform at) {
   return (geom::PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsPolygon, 9), at.instance));
}

geom::PathIterator Polygon::GetPathIterator(geom::AffineTransform at, double flatness) {
   return (geom::PathIterator)(tc::jEnv->CallObjectMethod(METHOD(clsPolygon, 10), at.instance, (jdouble)flatness));
}

bool Polygon::Intersects(double x, double y, double w, double h) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 11), (jdouble)x, (jdouble)y, (jdouble)w, (jdouble)h);
}

bool Polygon::Intersects(geom::Rectangle2D r) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPolygon, 12), r.instance);
}

void Polygon::Invalidate() {
   tc::jEnv->CallVoidMethod(METHOD(clsPolygon, 13));
}

void Polygon::Reset() {
   tc::jEnv->CallVoidMethod(METHOD(clsPolygon, 14));
}

void Polygon::Translate(int deltaX, int deltaY) {
   tc::jEnv->CallVoidMethod(METHOD(clsPolygon, 15), (jint)deltaX, (jint)deltaY);
}


// java/awt/Transparency
// Name: Transparency
// Base Class(es): [NativeObjectInstance]

NativeObject clsTransparency;
jclass InitTransparency() {
   return tcInitNativeObject(clsTransparency, "java/awt/Transparency", { 
          { "getTransparency", "()I", false }
       }, { 
       });
}

Transparency::Transparency(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitTransparency();
}

Transparency::~Transparency() {}

int Transparency::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsTransparency, 0));
}


// java/awt/color/ColorSpace
// Name: ColorSpace
// Base Class(es): [lang::Object]

NativeObject clsColorSpace;
jclass InitColorSpace() {
   return tcInitNativeObject(clsColorSpace, "java/awt/color/ColorSpace", { 
          { "fromCIEXYZ", "([F)[F", false }, 
          { "fromRGB", "([F)[F", false }, 
          { "getInstance", "(I)Ljava/awt/color/ColorSpace;", true }, 
          { "getMaxValue", "(I)F", false }, 
          { "getMinValue", "(I)F", false }, 
          { "getName", "(I)Ljava/lang/String;", false }, 
          { "getNumComponents", "()I", false }, 
          { "getType", "()I", false }, 
          { "isCS_sRGB", "()Z", false }, 
          { "toCIEXYZ", "([F)[F", false }, 
          { "toRGB", "([F)[F", false }
       }, { 
       });
}

ColorSpace::ColorSpace(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitColorSpace();
}

ColorSpace::~ColorSpace() {}

float* ColorSpace::FromCIEXYZ(int* size, float* colorvalue, int colorvaluec) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorSpace, 0), ToJFloatArray(colorvalue, colorvaluec)));
}

float* ColorSpace::FromRGB(int* size, float* rgbvalue, int rgbvaluec) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorSpace, 1), ToJFloatArray(rgbvalue, rgbvaluec)));
}

ColorSpace ColorSpace::GetInstance(int colorspace) {
   InitColorSpace();
   return (ColorSpace)(tc::jEnv->CallStaticObjectMethod(clsColorSpace.clazz, clsColorSpace.methods[2], (jint)colorspace));
}

float ColorSpace::GetMaxValue(int component) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsColorSpace, 3), (jint)component);
}

float ColorSpace::GetMinValue(int component) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsColorSpace, 4), (jint)component);
}

char* ColorSpace::GetName(int idx) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsColorSpace, 5), (jint)idx));
}

int ColorSpace::GetNumComponents() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorSpace, 6));
}

int ColorSpace::GetType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorSpace, 7));
}

bool ColorSpace::isCS_sRGB() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorSpace, 8));
}

float* ColorSpace::ToCIEXYZ(int* size, float* colorvalue, int colorvaluec) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorSpace, 9), ToJFloatArray(colorvalue, colorvaluec)));
}

float* ColorSpace::ToRGB(int* size, float* colorvalue, int colorvaluec) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorSpace, 10), ToJFloatArray(colorvalue, colorvaluec)));
}


// java/awt/image/renderable/RenderContext
// Name: RenderContext
// Base Class(es): [lang::Object]

NativeObject clsRenderContext;
jclass InitRenderContext() {
   return tcInitNativeObject(clsRenderContext, "java/awt/image/renderable/RenderContext", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "concatenateTransform", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "getAreaOfInterest", "()Ljava/awt/Shape;", false }, 
          { "getRenderingHints", "()Ljava/awt/RenderingHints;", false }, 
          { "getTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "preConcatenateTransform", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "setAreaOfInterest", "(Ljava/awt/Shape;)V", false }, 
          { "setRenderingHints", "(Ljava/awt/RenderingHints;)V", false }, 
          { "setTransform", "(Ljava/awt/geom/AffineTransform;)V", false }
       }, { 
       });
}

RenderContext::RenderContext(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitRenderContext();
}

RenderContext::~RenderContext() {}

RenderContext::RenderContext(geom::AffineTransform usr2dev)
 : instance(tc::jEnv->NewObject(InitRenderContext(), tc::jEnv->GetMethodID(InitRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;)V"), usr2dev.instance)), lang::Object(instance)
{}

RenderContext::RenderContext(geom::AffineTransform usr2dev, RenderingHints hints)
 : instance(tc::jEnv->NewObject(InitRenderContext(), tc::jEnv->GetMethodID(InitRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;Ljava/awt/RenderingHints;)V"), usr2dev.instance, hints.instance)), lang::Object(instance)
{}

RenderContext::RenderContext(geom::AffineTransform usr2dev, Shape aoi)
 : instance(tc::jEnv->NewObject(InitRenderContext(), tc::jEnv->GetMethodID(InitRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;Ljava/awt/Shape;)V"), usr2dev.instance, aoi.instance)), lang::Object(instance)
{}

RenderContext::RenderContext(geom::AffineTransform usr2dev, Shape aoi, RenderingHints hints)
 : instance(tc::jEnv->NewObject(InitRenderContext(), tc::jEnv->GetMethodID(InitRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;Ljava/awt/Shape;Ljava/awt/RenderingHints;)V"), usr2dev.instance, aoi.instance, hints.instance)), lang::Object(instance)
{}

lang::Object RenderContext::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderContext, 0)));
}

void RenderContext::ConcatenateTransform(geom::AffineTransform modTransform) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderContext, 1), modTransform.instance);
}

Shape RenderContext::GetAreaOfInterest() {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsRenderContext, 2)));
}

RenderingHints RenderContext::GetRenderingHints() {
   return (RenderingHints)(tc::jEnv->CallObjectMethod(METHOD(clsRenderContext, 3)));
}

geom::AffineTransform RenderContext::GetTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsRenderContext, 4)));
}

void RenderContext::PreConcatenateTransform(geom::AffineTransform modTransform) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderContext, 5), modTransform.instance);
}

void RenderContext::SetAreaOfInterest(Shape newAoi) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderContext, 6), newAoi.instance);
}

void RenderContext::SetRenderingHints(RenderingHints hints) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderContext, 7), hints.instance);
}

void RenderContext::SetTransform(geom::AffineTransform newTransform) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderContext, 8), newTransform.instance);
}


// java/awt/image/renderable/RenderableImage
// Name: RenderableImage
// Base Class(es): [NativeObjectInstance]

NativeObject clsRenderableImage;
jclass InitRenderableImage() {
   return tcInitNativeObject(clsRenderableImage, "java/awt/image/renderable/RenderableImage", { 
          { "createDefaultRendering", "()Ljava/awt/image/RenderedImage;", false }, 
          { "createRendering", "(Ljava/awt/image/renderable/RenderContext;)Ljava/awt/image/RenderedImage;", false }, 
          { "createScaledRendering", "(IILjava/awt/RenderingHints;)Ljava/awt/image/RenderedImage;", false }, 
          { "getHeight", "()F", false }, 
          { "getMinX", "()F", false }, 
          { "getMinY", "()F", false }, 
          { "getProperty", "(Ljava/lang/String;)Ljava/lang/Object;", false }, 
          { "getPropertyNames", "()[Ljava/lang/String;", false }, 
          { "getSources", "()Ljava/util/Vector;", false }, 
          { "getWidth", "()F", false }, 
          { "isDynamic", "()Z", false }
       }, { 
       });
}

RenderableImage::RenderableImage(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitRenderableImage();
}

RenderableImage::~RenderableImage() {}

RenderedImage RenderableImage::CreateDefaultRendering() {
   return (RenderedImage)(tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 0)));
}

RenderedImage RenderableImage::CreateRendering(RenderContext renderContext) {
   return (RenderedImage)(tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 1), renderContext.instance));
}

RenderedImage RenderableImage::CreateScaledRendering(int w, int h, RenderingHints hints) {
   return (RenderedImage)(tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 2), (jint)w, (jint)h, hints.instance));
}

float RenderableImage::GetHeight() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRenderableImage, 3));
}

float RenderableImage::GetMinX() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRenderableImage, 4));
}

float RenderableImage::GetMinY() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRenderableImage, 5));
}

lang::Object RenderableImage::GetProperty(const char* name) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 6), ToJString(name)));
}

char** RenderableImage::GetPropertyNames(int* size) {
   return FromJStringArray(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 7)));
}

util::Vector RenderableImage::GetSources() {
   return (util::Vector)(tc::jEnv->CallObjectMethod(METHOD(clsRenderableImage, 8)));
}

float RenderableImage::GetWidth() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRenderableImage, 9));
}

bool RenderableImage::IsDynamic() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderableImage, 10));
}


// java/awt/image/DataBuffer
// Name: DataBuffer
// Base Class(es): [lang::Object]

NativeObject clsDataBuffer;
jclass InitDataBuffer() {
   return tcInitNativeObject(clsDataBuffer, "java/awt/image/DataBuffer", { 
          { "getDataType", "()I", false }, 
          { "getDataTypeSize", "(I)I", true }, 
          { "getElem", "(I)I", false }, 
          { "getElem", "(II)I", false }, 
          { "getElemDouble", "(I)D", false }, 
          { "getElemDouble", "(II)D", false }, 
          { "getElemFloat", "(I)F", false }, 
          { "getElemFloat", "(II)F", false }, 
          { "getNumBanks", "()I", false }, 
          { "getOffset", "()I", false }, 
          { "getOffsets", "()[I", false }, 
          { "getSize", "()I", false }, 
          { "setElem", "(II)V", false }, 
          { "setElem", "(III)V", false }, 
          { "setElemDouble", "(ID)V", false }, 
          { "setElemDouble", "(IID)V", false }, 
          { "setElemFloat", "(IF)V", false }, 
          { "setElemFloat", "(IIF)V", false }
       }, { 
       });
}

DataBuffer::DataBuffer(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitDataBuffer();
}

DataBuffer::~DataBuffer() {}

DataBuffer::DataBuffer(int dataType, int size)
 : instance(tc::jEnv->NewObject(InitDataBuffer(), tc::jEnv->GetMethodID(InitDataBuffer(), "<init>", "(II)V"), (jint)dataType, (jint)size)), lang::Object(instance)
{}

DataBuffer::DataBuffer(int dataType, int size, int numBanks)
 : instance(tc::jEnv->NewObject(InitDataBuffer(), tc::jEnv->GetMethodID(InitDataBuffer(), "<init>", "(III)V"), (jint)dataType, (jint)size, (jint)numBanks)), lang::Object(instance)
{}

DataBuffer::DataBuffer(int dataType, int size, int numBanks, int offset)
 : instance(tc::jEnv->NewObject(InitDataBuffer(), tc::jEnv->GetMethodID(InitDataBuffer(), "<init>", "(IIII)V"), (jint)dataType, (jint)size, (jint)numBanks, (jint)offset)), lang::Object(instance)
{}

DataBuffer::DataBuffer(int dataType, int size, int numBanks, int* offsets, int offsetsc)
 : instance(tc::jEnv->NewObject(InitDataBuffer(), tc::jEnv->GetMethodID(InitDataBuffer(), "<init>", "(III[I)V"), (jint)dataType, (jint)size, (jint)numBanks, ToJIntArray(offsets, offsetsc))), lang::Object(instance)
{}

int DataBuffer::GetDataType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 0));
}

int DataBuffer::GetDataTypeSize(int type) {
   InitDataBuffer();
   return (int)tc::jEnv->CallStaticIntMethod(clsDataBuffer.clazz, clsDataBuffer.methods[1], (jint)type);
}

int DataBuffer::GetElem(int i) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 2), (jint)i);
}

int DataBuffer::GetElem(int bank, int i) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 3), (jint)bank, (jint)i);
}

double DataBuffer::GetElemDouble(int i) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDataBuffer, 4), (jint)i);
}

double DataBuffer::GetElemDouble(int bank, int i) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsDataBuffer, 5), (jint)bank, (jint)i);
}

float DataBuffer::GetElemFloat(int i) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsDataBuffer, 6), (jint)i);
}

float DataBuffer::GetElemFloat(int bank, int i) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsDataBuffer, 7), (jint)bank, (jint)i);
}

int DataBuffer::GetNumBanks() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 8));
}

int DataBuffer::GetOffset() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 9));
}

int* DataBuffer::GetOffsets(int* size) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsDataBuffer, 10)));
}

int DataBuffer::GetSize() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDataBuffer, 11));
}

void DataBuffer::SetElem(int i, int val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 12), (jint)i, (jint)val);
}

void DataBuffer::SetElem(int bank, int i, int val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 13), (jint)bank, (jint)i, (jint)val);
}

void DataBuffer::SetElemDouble(int i, double val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 14), (jint)i, (jdouble)val);
}

void DataBuffer::SetElemDouble(int bank, int i, double val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 15), (jint)bank, (jint)i, (jdouble)val);
}

void DataBuffer::SetElemFloat(int i, float val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 16), (jint)i, (jfloat)val);
}

void DataBuffer::SetElemFloat(int bank, int i, float val) {
   tc::jEnv->CallVoidMethod(METHOD(clsDataBuffer, 17), (jint)bank, (jint)i, (jfloat)val);
}


// java/awt/image/SampleModel
// Name: SampleModel
// Base Class(es): [lang::Object]

NativeObject clsSampleModel;
jclass InitSampleModel() {
   return tcInitNativeObject(clsSampleModel, "java/awt/image/SampleModel", { 
          { "createCompatibleSampleModel", "(II)Ljava/awt/image/SampleModel;", false }, 
          { "createDataBuffer", "()Ljava/awt/image/DataBuffer;", false }, 
          { "createSubsetSampleModel", "([I)Ljava/awt/image/SampleModel;", false }, 
          { "getDataElements", "(IIIILjava/lang/Object;Ljava/awt/image/DataBuffer;)Ljava/lang/Object;", false }, 
          { "getDataElements", "(IILjava/lang/Object;Ljava/awt/image/DataBuffer;)Ljava/lang/Object;", false }, 
          { "getDataType", "()I", false }, 
          { "getHeight", "()I", false }, 
          { "getNumBands", "()I", false }, 
          { "getNumDataElements", "()I", false }, 
          { "getPixel", "(II[DLjava/awt/image/DataBuffer;)[D", false }, 
          { "getPixel", "(II[FLjava/awt/image/DataBuffer;)[F", false }, 
          { "getPixel", "(II[ILjava/awt/image/DataBuffer;)[I", false }, 
          { "getPixels", "(IIII[DLjava/awt/image/DataBuffer;)[D", false }, 
          { "getPixels", "(IIII[FLjava/awt/image/DataBuffer;)[F", false }, 
          { "getPixels", "(IIII[ILjava/awt/image/DataBuffer;)[I", false }, 
          { "getSample", "(IIILjava/awt/image/DataBuffer;)I", false }, 
          { "getSampleDouble", "(IIILjava/awt/image/DataBuffer;)D", false }, 
          { "getSampleFloat", "(IIILjava/awt/image/DataBuffer;)F", false }, 
          { "getSamples", "(IIIII[DLjava/awt/image/DataBuffer;)[D", false }, 
          { "getSamples", "(IIIII[FLjava/awt/image/DataBuffer;)[F", false }, 
          { "getSamples", "(IIIII[ILjava/awt/image/DataBuffer;)[I", false }, 
          { "getSampleSize", "()[I", false }, 
          { "getSampleSize", "(I)I", false }, 
          { "getTransferType", "()I", false }, 
          { "getWidth", "()I", false }, 
          { "setDataElements", "(IIIILjava/lang/Object;Ljava/awt/image/DataBuffer;)V", false }, 
          { "setDataElements", "(IILjava/lang/Object;Ljava/awt/image/DataBuffer;)V", false }, 
          { "setPixel", "(II[DLjava/awt/image/DataBuffer;)V", false }, 
          { "setPixel", "(II[FLjava/awt/image/DataBuffer;)V", false }, 
          { "setPixel", "(II[ILjava/awt/image/DataBuffer;)V", false }, 
          { "setPixels", "(IIII[DLjava/awt/image/DataBuffer;)V", false }, 
          { "setPixels", "(IIII[FLjava/awt/image/DataBuffer;)V", false }, 
          { "setPixels", "(IIII[ILjava/awt/image/DataBuffer;)V", false }, 
          { "setSample", "(IIIDLjava/awt/image/DataBuffer;)V", false }, 
          { "setSample", "(IIIFLjava/awt/image/DataBuffer;)V", false }, 
          { "setSample", "(IIIILjava/awt/image/DataBuffer;)V", false }, 
          { "setSamples", "(IIIII[DLjava/awt/image/DataBuffer;)V", false }, 
          { "setSamples", "(IIIII[FLjava/awt/image/DataBuffer;)V", false }, 
          { "setSamples", "(IIIII[ILjava/awt/image/DataBuffer;)V", false }
       }, { 
       });
}

SampleModel::SampleModel(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitSampleModel();
}

SampleModel::~SampleModel() {}

SampleModel SampleModel::CreateCompatibleSampleModel(int w, int h) {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 0), (jint)w, (jint)h));
}

DataBuffer SampleModel::CreateDataBuffer() {
   return (DataBuffer)(tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 1)));
}

SampleModel SampleModel::CreateSubsetSampleModel(int* bands, int bandsc) {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 2), ToJIntArray(bands, bandsc)));
}

lang::Object SampleModel::GetDataElements(int x, int y, int w, int h, lang::Object obj, DataBuffer data) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 3), (jint)x, (jint)y, (jint)w, (jint)h, obj.instance, data.instance));
}

lang::Object SampleModel::GetDataElements(int x, int y, lang::Object obj, DataBuffer data) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 4), (jint)x, (jint)y, obj.instance, data.instance));
}

int SampleModel::GetDataType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 5));
}

int SampleModel::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 6));
}

int SampleModel::GetNumBands() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 7));
}

int SampleModel::GetNumDataElements() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 8));
}

double* SampleModel::GetPixel(int* size, int x, int y, double* dArray, int dArrayc, DataBuffer data) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 9), (jint)x, (jint)y, ToJDoubleArray(dArray, dArrayc), data.instance));
}

float* SampleModel::GetPixel(int* size, int x, int y, float* fArray, int fArrayc, DataBuffer data) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 10), (jint)x, (jint)y, ToJFloatArray(fArray, fArrayc), data.instance));
}

int* SampleModel::GetPixel(int* size, int x, int y, int* iArray, int iArrayc, DataBuffer data) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 11), (jint)x, (jint)y, ToJIntArray(iArray, iArrayc), data.instance));
}

double* SampleModel::GetPixels(int* size, int x, int y, int w, int h, double* dArray, int dArrayc, DataBuffer data) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 12), (jint)x, (jint)y, (jint)w, (jint)h, ToJDoubleArray(dArray, dArrayc), data.instance));
}

float* SampleModel::GetPixels(int* size, int x, int y, int w, int h, float* fArray, int fArrayc, DataBuffer data) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 13), (jint)x, (jint)y, (jint)w, (jint)h, ToJFloatArray(fArray, fArrayc), data.instance));
}

int* SampleModel::GetPixels(int* size, int x, int y, int w, int h, int* iArray, int iArrayc, DataBuffer data) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 14), (jint)x, (jint)y, (jint)w, (jint)h, ToJIntArray(iArray, iArrayc), data.instance));
}

int SampleModel::GetSample(int x, int y, int b, DataBuffer data) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 15), (jint)x, (jint)y, (jint)b, data.instance);
}

double SampleModel::GetSampleDouble(int x, int y, int b, DataBuffer data) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsSampleModel, 16), (jint)x, (jint)y, (jint)b, data.instance);
}

float SampleModel::GetSampleFloat(int x, int y, int b, DataBuffer data) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsSampleModel, 17), (jint)x, (jint)y, (jint)b, data.instance);
}

double* SampleModel::GetSamples(int* size, int x, int y, int w, int h, int b, double* dArray, int dArrayc, DataBuffer data) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 18), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJDoubleArray(dArray, dArrayc), data.instance));
}

float* SampleModel::GetSamples(int* size, int x, int y, int w, int h, int b, float* fArray, int fArrayc, DataBuffer data) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 19), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJFloatArray(fArray, fArrayc), data.instance));
}

int* SampleModel::GetSamples(int* size, int x, int y, int w, int h, int b, int* iArray, int iArrayc, DataBuffer data) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 20), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJIntArray(iArray, iArrayc), data.instance));
}

int* SampleModel::GetSampleSize(int* size) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsSampleModel, 21)));
}

int SampleModel::GetSampleSize(int band) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 22), (jint)band);
}

int SampleModel::GetTransferType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 23));
}

int SampleModel::GetWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSampleModel, 24));
}

void SampleModel::SetDataElements(int x, int y, int w, int h, lang::Object obj, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 25), (jint)x, (jint)y, (jint)w, (jint)h, obj.instance, data.instance);
}

void SampleModel::SetDataElements(int x, int y, lang::Object obj, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 26), (jint)x, (jint)y, obj.instance, data.instance);
}

void SampleModel::SetPixel(int x, int y, double* dArray, int dArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 27), (jint)x, (jint)y, ToJDoubleArray(dArray, dArrayc), data.instance);
}

void SampleModel::SetPixel(int x, int y, float* fArray, int fArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 28), (jint)x, (jint)y, ToJFloatArray(fArray, fArrayc), data.instance);
}

void SampleModel::SetPixel(int x, int y, int* iArray, int iArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 29), (jint)x, (jint)y, ToJIntArray(iArray, iArrayc), data.instance);
}

void SampleModel::SetPixels(int x, int y, int w, int h, double* dArray, int dArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 30), (jint)x, (jint)y, (jint)w, (jint)h, ToJDoubleArray(dArray, dArrayc), data.instance);
}

void SampleModel::SetPixels(int x, int y, int w, int h, float* fArray, int fArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 31), (jint)x, (jint)y, (jint)w, (jint)h, ToJFloatArray(fArray, fArrayc), data.instance);
}

void SampleModel::SetPixels(int x, int y, int w, int h, int* iArray, int iArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 32), (jint)x, (jint)y, (jint)w, (jint)h, ToJIntArray(iArray, iArrayc), data.instance);
}

void SampleModel::SetSample(int x, int y, int b, double s, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 33), (jint)x, (jint)y, (jint)b, (jdouble)s, data.instance);
}

void SampleModel::SetSample(int x, int y, int b, float s, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 34), (jint)x, (jint)y, (jint)b, (jfloat)s, data.instance);
}

void SampleModel::SetSample(int x, int y, int b, int s, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 35), (jint)x, (jint)y, (jint)b, (jint)s, data.instance);
}

void SampleModel::SetSamples(int x, int y, int w, int h, int b, double* dArray, int dArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 36), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJDoubleArray(dArray, dArrayc), data.instance);
}

void SampleModel::SetSamples(int x, int y, int w, int h, int b, float* fArray, int fArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 37), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJFloatArray(fArray, fArrayc), data.instance);
}

void SampleModel::SetSamples(int x, int y, int w, int h, int b, int* iArray, int iArrayc, DataBuffer data) {
   tc::jEnv->CallVoidMethod(METHOD(clsSampleModel, 38), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJIntArray(iArray, iArrayc), data.instance);
}


// java/awt/image/Raster
// Name: Raster
// Base Class(es): [lang::Object]

NativeObject clsRaster;
jclass InitRaster() {
   return tcInitNativeObject(clsRaster, "java/awt/image/Raster", { 
          { "createBandedRaster", "(Ljava/awt/image/DataBuffer;III[I[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createBandedRaster", "(IIII[I[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createBandedRaster", "(IIIILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createChild", "(IIIIII[I)Ljava/awt/image/Raster;", false }, 
          { "createCompatibleWritableRaster", "()Ljava/awt/image/WritableRaster;", false }, 
          { "createCompatibleWritableRaster", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "createCompatibleWritableRaster", "(IIII)Ljava/awt/image/WritableRaster;", false }, 
          { "createCompatibleWritableRaster", "(Ljava/awt/Rectangle;)Ljava/awt/image/WritableRaster;", false }, 
          { "createInterleavedRaster", "(Ljava/awt/image/DataBuffer;IIII[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createInterleavedRaster", "(IIIII[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createInterleavedRaster", "(IIIILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createPackedRaster", "(Ljava/awt/image/DataBuffer;III[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createPackedRaster", "(Ljava/awt/image/DataBuffer;IIILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createPackedRaster", "(III[ILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createPackedRaster", "(IIIIILjava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createRaster", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Point;)Ljava/awt/image/Raster;", true }, 
          { "createTranslatedChild", "(II)Ljava/awt/image/Raster;", false }, 
          { "createWritableRaster", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "createWritableRaster", "(Ljava/awt/image/SampleModel;Ljava/awt/Point;)Ljava/awt/image/WritableRaster;", true }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getDataBuffer", "()Ljava/awt/image/DataBuffer;", false }, 
          { "getDataElements", "(IIIILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getDataElements", "(IILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getHeight", "()I", false }, 
          { "getMinX", "()I", false }, 
          { "getMinY", "()I", false }, 
          { "getNumBands", "()I", false }, 
          { "getNumDataElements", "()I", false }, 
          { "getParent", "()Ljava/awt/image/Raster;", false }, 
          { "getPixel", "(II[D)[D", false }, 
          { "getPixel", "(II[F)[F", false }, 
          { "getPixel", "(II[I)[I", false }, 
          { "getPixels", "(IIII[D)[D", false }, 
          { "getPixels", "(IIII[F)[F", false }, 
          { "getPixels", "(IIII[I)[I", false }, 
          { "getSample", "(III)I", false }, 
          { "getSampleDouble", "(III)D", false }, 
          { "getSampleFloat", "(III)F", false }, 
          { "getSampleModel", "()Ljava/awt/image/SampleModel;", false }, 
          { "getSampleModelTranslateX", "()I", false }, 
          { "getSampleModelTranslateY", "()I", false }, 
          { "getSamples", "(IIIII[D)[D", false }, 
          { "getSamples", "(IIIII[F)[F", false }, 
          { "getSamples", "(IIIII[I)[I", false }, 
          { "getTransferType", "()I", false }, 
          { "getWidth", "()I", false }
       }, { 
       });
}

Raster::Raster(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitRaster();
}

Raster::~Raster() {}

Raster::Raster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin)
 : instance(tc::jEnv->NewObject(InitRaster(), tc::jEnv->GetMethodID(InitRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Point;)V"), sampleModel.instance, dataBuffer.instance, origin.instance)), lang::Object(instance)
{}

Raster::Raster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, Raster parent)
 : instance(tc::jEnv->NewObject(InitRaster(), tc::jEnv->GetMethodID(InitRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Rectangle;Ljava/awt/Point;Ljava/awt/image/Raster;)V"), sampleModel.instance, dataBuffer.instance, aRegion.instance, sampleModelTranslate.instance, parent.instance)), lang::Object(instance)
{}

Raster::Raster(SampleModel sampleModel, Point origin)
 : instance(tc::jEnv->NewObject(InitRaster(), tc::jEnv->GetMethodID(InitRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/Point;)V"), sampleModel.instance, origin.instance)), lang::Object(instance)
{}

WritableRaster Raster::CreateBandedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int* bankIndices, int bankIndicesc, int* bandOffsets, int bandOffsetsc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[0], dataBuffer.instance, (jint)w, (jint)h, (jint)scanlineStride, ToJIntArray(bankIndices, bankIndicesc), ToJIntArray(bandOffsets, bandOffsetsc), location.instance));
}

WritableRaster Raster::CreateBandedRaster(int dataType, int w, int h, int scanlineStride, int* bankIndices, int bankIndicesc, int* bandOffsets, int bandOffsetsc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[1], (jint)dataType, (jint)w, (jint)h, (jint)scanlineStride, ToJIntArray(bankIndices, bankIndicesc), ToJIntArray(bandOffsets, bandOffsetsc), location.instance));
}

WritableRaster Raster::CreateBandedRaster(int dataType, int w, int h, int bands, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[2], (jint)dataType, (jint)w, (jint)h, (jint)bands, location.instance));
}

Raster Raster::CreateChild(int parentX, int parentY, int width, int height, int childMinX, int childMinY, int* bandList, int bandListc) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 3), (jint)parentX, (jint)parentY, (jint)width, (jint)height, (jint)childMinX, (jint)childMinY, ToJIntArray(bandList, bandListc)));
}

WritableRaster Raster::CreateCompatibleWritableRaster() {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 4)));
}

WritableRaster Raster::CreateCompatibleWritableRaster(int w, int h) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 5), (jint)w, (jint)h));
}

WritableRaster Raster::CreateCompatibleWritableRaster(int x, int y, int w, int h) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 6), (jint)x, (jint)y, (jint)w, (jint)h));
}

WritableRaster Raster::CreateCompatibleWritableRaster(Rectangle rect) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 7), rect.instance));
}

WritableRaster Raster::CreateInterleavedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int pixelStride, int* bandOffsets, int bandOffsetsc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[8], dataBuffer.instance, (jint)w, (jint)h, (jint)scanlineStride, (jint)pixelStride, ToJIntArray(bandOffsets, bandOffsetsc), location.instance));
}

WritableRaster Raster::CreateInterleavedRaster(int dataType, int w, int h, int scanlineStride, int pixelStride, int* bandOffsets, int bandOffsetsc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[9], (jint)dataType, (jint)w, (jint)h, (jint)scanlineStride, (jint)pixelStride, ToJIntArray(bandOffsets, bandOffsetsc), location.instance));
}

WritableRaster Raster::CreateInterleavedRaster(int dataType, int w, int h, int bands, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[10], (jint)dataType, (jint)w, (jint)h, (jint)bands, location.instance));
}

WritableRaster Raster::CreatePackedRaster(DataBuffer dataBuffer, int w, int h, int scanlineStride, int* bandMasks, int bandMasksc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[11], dataBuffer.instance, (jint)w, (jint)h, (jint)scanlineStride, ToJIntArray(bandMasks, bandMasksc), location.instance));
}

WritableRaster Raster::CreatePackedRaster(DataBuffer dataBuffer, int w, int h, int bitsPerPixel, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[12], dataBuffer.instance, (jint)w, (jint)h, (jint)bitsPerPixel, location.instance));
}

WritableRaster Raster::CreatePackedRaster(int dataType, int w, int h, int* bandMasks, int bandMasksc, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[13], (jint)dataType, (jint)w, (jint)h, ToJIntArray(bandMasks, bandMasksc), location.instance));
}

WritableRaster Raster::CreatePackedRaster(int dataType, int w, int h, int bands, int bitsPerBand, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[14], (jint)dataType, (jint)w, (jint)h, (jint)bands, (jint)bitsPerBand, location.instance));
}

Raster Raster::CreateRaster(SampleModel sm, DataBuffer db, Point location) {
   InitRaster();
   return (Raster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[15], sm.instance, db.instance, location.instance));
}

Raster Raster::CreateTranslatedChild(int childMinX, int childMinY) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 16), (jint)childMinX, (jint)childMinY));
}

WritableRaster Raster::CreateWritableRaster(SampleModel sm, DataBuffer db, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[17], sm.instance, db.instance, location.instance));
}

WritableRaster Raster::CreateWritableRaster(SampleModel sm, Point location) {
   InitRaster();
   return (WritableRaster)(tc::jEnv->CallStaticObjectMethod(clsRaster.clazz, clsRaster.methods[18], sm.instance, location.instance));
}

Rectangle Raster::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 19)));
}

DataBuffer Raster::GetDataBuffer() {
   return (DataBuffer)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 20)));
}

lang::Object Raster::GetDataElements(int x, int y, int w, int h, lang::Object outData) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 21), (jint)x, (jint)y, (jint)w, (jint)h, outData.instance));
}

lang::Object Raster::GetDataElements(int x, int y, lang::Object outData) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 22), (jint)x, (jint)y, outData.instance));
}

int Raster::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 23));
}

int Raster::GetMinX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 24));
}

int Raster::GetMinY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 25));
}

int Raster::GetNumBands() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 26));
}

int Raster::GetNumDataElements() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 27));
}

Raster Raster::GetParent() {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 28)));
}

double* Raster::GetPixel(int* size, int x, int y, double* dArray, int dArrayc) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 29), (jint)x, (jint)y, ToJDoubleArray(dArray, dArrayc)));
}

float* Raster::GetPixel(int* size, int x, int y, float* fArray, int fArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 30), (jint)x, (jint)y, ToJFloatArray(fArray, fArrayc)));
}

int* Raster::GetPixel(int* size, int x, int y, int* iArray, int iArrayc) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 31), (jint)x, (jint)y, ToJIntArray(iArray, iArrayc)));
}

double* Raster::GetPixels(int* size, int x, int y, int w, int h, double* dArray, int dArrayc) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 32), (jint)x, (jint)y, (jint)w, (jint)h, ToJDoubleArray(dArray, dArrayc)));
}

float* Raster::GetPixels(int* size, int x, int y, int w, int h, float* fArray, int fArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 33), (jint)x, (jint)y, (jint)w, (jint)h, ToJFloatArray(fArray, fArrayc)));
}

int* Raster::GetPixels(int* size, int x, int y, int w, int h, int* iArray, int iArrayc) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 34), (jint)x, (jint)y, (jint)w, (jint)h, ToJIntArray(iArray, iArrayc)));
}

int Raster::GetSample(int x, int y, int b) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 35), (jint)x, (jint)y, (jint)b);
}

double Raster::GetSampleDouble(int x, int y, int b) {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRaster, 36), (jint)x, (jint)y, (jint)b);
}

float Raster::GetSampleFloat(int x, int y, int b) {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRaster, 37), (jint)x, (jint)y, (jint)b);
}

SampleModel Raster::GetSampleModel() {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsRaster, 38)));
}

int Raster::GetSampleModelTranslateX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 39));
}

int Raster::GetSampleModelTranslateY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 40));
}

double* Raster::GetSamples(int* size, int x, int y, int w, int h, int b, double* dArray, int dArrayc) {
   return FromJDoubleArray(size, (jdoubleArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 41), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJDoubleArray(dArray, dArrayc)));
}

float* Raster::GetSamples(int* size, int x, int y, int w, int h, int b, float* fArray, int fArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 42), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJFloatArray(fArray, fArrayc)));
}

int* Raster::GetSamples(int* size, int x, int y, int w, int h, int b, int* iArray, int iArrayc) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsRaster, 43), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJIntArray(iArray, iArrayc)));
}

int Raster::GetTransferType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 44));
}

int Raster::GetWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRaster, 45));
}


// java/awt/image/WritableRaster
// Name: WritableRaster
// Base Class(es): [Raster]

NativeObject clsWritableRaster;
jclass InitWritableRaster() {
   return tcInitNativeObject(clsWritableRaster, "java/awt/image/WritableRaster", { 
          { "createWritableChild", "(IIIIII[I)Ljava/awt/image/WritableRaster;", false }, 
          { "createWritableTranslatedChild", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "getWritableParent", "()Ljava/awt/image/WritableRaster;", false }, 
          { "setDataElements", "(IIIILjava/lang/Object;)V", false }, 
          { "setDataElements", "(IILjava/lang/Object;)V", false }, 
          { "setDataElements", "(IILjava/awt/image/Raster;)V", false }, 
          { "setPixel", "(II[D)V", false }, 
          { "setPixel", "(II[F)V", false }, 
          { "setPixel", "(II[I)V", false }, 
          { "setPixels", "(IIII[D)V", false }, 
          { "setPixels", "(IIII[F)V", false }, 
          { "setPixels", "(IIII[I)V", false }, 
          { "setRect", "(IILjava/awt/image/Raster;)V", false }, 
          { "setRect", "(Ljava/awt/image/Raster;)V", false }, 
          { "setSample", "(IIID)V", false }, 
          { "setSample", "(IIIF)V", false }, 
          { "setSample", "(IIII)V", false }, 
          { "setSamples", "(IIIII[D)V", false }, 
          { "setSamples", "(IIIII[F)V", false }, 
          { "setSamples", "(IIIII[I)V", false }
       }, { 
       });
}

WritableRaster::WritableRaster(jobject instance)
 : instance(instance), Raster(instance) {
    InitWritableRaster();
}

WritableRaster::~WritableRaster() {}

WritableRaster::WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin)
 : instance(tc::jEnv->NewObject(InitWritableRaster(), tc::jEnv->GetMethodID(InitWritableRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Point;)V"), sampleModel.instance, dataBuffer.instance, origin.instance)), Raster(instance)
{}

WritableRaster::WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, WritableRaster parent)
 : instance(tc::jEnv->NewObject(InitWritableRaster(), tc::jEnv->GetMethodID(InitWritableRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/image/DataBuffer;Ljava/awt/Rectangle;Ljava/awt/Point;Ljava/awt/image/WritableRaster;)V"), sampleModel.instance, dataBuffer.instance, aRegion.instance, sampleModelTranslate.instance, parent.instance)), Raster(instance)
{}

WritableRaster::WritableRaster(SampleModel sampleModel, Point origin)
 : instance(tc::jEnv->NewObject(InitWritableRaster(), tc::jEnv->GetMethodID(InitWritableRaster(), "<init>", "(Ljava/awt/image/SampleModel;Ljava/awt/Point;)V"), sampleModel.instance, origin.instance)), Raster(instance)
{}

WritableRaster WritableRaster::CreateWritableChild(int parentX, int parentY, int w, int h, int childMinX, int childMinY, int* bandList, int bandListc) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsWritableRaster, 0), (jint)parentX, (jint)parentY, (jint)w, (jint)h, (jint)childMinX, (jint)childMinY, ToJIntArray(bandList, bandListc)));
}

WritableRaster WritableRaster::CreateWritableTranslatedChild(int childMinX, int childMinY) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsWritableRaster, 1), (jint)childMinX, (jint)childMinY));
}

WritableRaster WritableRaster::GetWritableParent() {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsWritableRaster, 2)));
}

void WritableRaster::SetDataElements(int x, int y, int w, int h, lang::Object inData) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 3), (jint)x, (jint)y, (jint)w, (jint)h, inData.instance);
}

void WritableRaster::SetDataElements(int x, int y, lang::Object inData) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 4), (jint)x, (jint)y, inData.instance);
}

void WritableRaster::SetDataElements(int x, int y, Raster inRaster) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 5), (jint)x, (jint)y, inRaster.instance);
}

void WritableRaster::SetPixel(int x, int y, double* dArray, int dArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 6), (jint)x, (jint)y, ToJDoubleArray(dArray, dArrayc));
}

void WritableRaster::SetPixel(int x, int y, float* fArray, int fArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 7), (jint)x, (jint)y, ToJFloatArray(fArray, fArrayc));
}

void WritableRaster::SetPixel(int x, int y, int* iArray, int iArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 8), (jint)x, (jint)y, ToJIntArray(iArray, iArrayc));
}

void WritableRaster::SetPixels(int x, int y, int w, int h, double* dArray, int dArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 9), (jint)x, (jint)y, (jint)w, (jint)h, ToJDoubleArray(dArray, dArrayc));
}

void WritableRaster::SetPixels(int x, int y, int w, int h, float* fArray, int fArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 10), (jint)x, (jint)y, (jint)w, (jint)h, ToJFloatArray(fArray, fArrayc));
}

void WritableRaster::SetPixels(int x, int y, int w, int h, int* iArray, int iArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 11), (jint)x, (jint)y, (jint)w, (jint)h, ToJIntArray(iArray, iArrayc));
}

void WritableRaster::SetRect(int dx, int dy, Raster srcRaster) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 12), (jint)dx, (jint)dy, srcRaster.instance);
}

void WritableRaster::SetRect(Raster srcRaster) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 13), srcRaster.instance);
}

void WritableRaster::SetSample(int x, int y, int b, double s) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 14), (jint)x, (jint)y, (jint)b, (jdouble)s);
}

void WritableRaster::SetSample(int x, int y, int b, float s) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 15), (jint)x, (jint)y, (jint)b, (jfloat)s);
}

void WritableRaster::SetSample(int x, int y, int b, int s) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 16), (jint)x, (jint)y, (jint)b, (jint)s);
}

void WritableRaster::SetSamples(int x, int y, int w, int h, int b, double* dArray, int dArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 17), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJDoubleArray(dArray, dArrayc));
}

void WritableRaster::SetSamples(int x, int y, int w, int h, int b, float* fArray, int fArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 18), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJFloatArray(fArray, fArrayc));
}

void WritableRaster::SetSamples(int x, int y, int w, int h, int b, int* iArray, int iArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRaster, 19), (jint)x, (jint)y, (jint)w, (jint)h, (jint)b, ToJIntArray(iArray, iArrayc));
}


// java/awt/image/ColorModel
// Name: ColorModel
// Base Class(es): [Transparency, lang::Object]

NativeObject clsColorModel;
jclass InitColorModel() {
   return tcInitNativeObject(clsColorModel, "java/awt/image/ColorModel", { 
          { "coerceData", "(Ljava/awt/image/WritableRaster;Z)Ljava/awt/image/ColorModel;", false }, 
          { "createCompatibleSampleModel", "(II)Ljava/awt/image/SampleModel;", false }, 
          { "createCompatibleWritableRaster", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "finalize", "()V", false }, 
          { "getAlpha", "(I)I", false }, 
          { "getAlpha", "(Ljava/lang/Object;)I", false }, 
          { "getAlphaRaster", "(Ljava/awt/image/WritableRaster;)Ljava/awt/image/WritableRaster;", false }, 
          { "getBlue", "(I)I", false }, 
          { "getBlue", "(Ljava/lang/Object;)I", false }, 
          { "getColorSpace", "()Ljava/awt/color/ColorSpace;", false }, 
          { "getComponents", "(I[II)[I", false }, 
          { "getComponents", "(Ljava/lang/Object;[II)[I", false }, 
          { "getComponentSize", "()[I", false }, 
          { "getComponentSize", "(I)I", false }, 
          { "getDataElement", "([FI)I", false }, 
          { "getDataElement", "([II)I", false }, 
          { "getDataElements", "([FILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getDataElements", "([IILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getDataElements", "(ILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getGreen", "(I)I", false }, 
          { "getGreen", "(Ljava/lang/Object;)I", false }, 
          { "getNormalizedComponents", "([II[FI)[F", false }, 
          { "getNormalizedComponents", "(Ljava/lang/Object;[FI)[F", false }, 
          { "getNumColorComponents", "()I", false }, 
          { "getNumComponents", "()I", false }, 
          { "getPixelSize", "()I", false }, 
          { "getRed", "(I)I", false }, 
          { "getRed", "(Ljava/lang/Object;)I", false }, 
          { "getRGB", "(I)I", false }, 
          { "getRGB", "(Ljava/lang/Object;)I", false }, 
          { "getRGBdefault", "()Ljava/awt/image/ColorModel;", true }, 
          { "getTransferType", "()I", false }, 
          { "getTransparency", "()I", false }, 
          { "getUnnormalizedComponents", "([FI[II)[I", false }, 
          { "hasAlpha", "()Z", false }, 
          { "hashCode", "()I", false }, 
          { "isAlphaPremultiplied", "()Z", false }, 
          { "isCompatibleRaster", "(Ljava/awt/image/Raster;)Z", false }, 
          { "isCompatibleSampleModel", "(Ljava/awt/image/SampleModel;)Z", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

ColorModel::ColorModel(jobject instance)
 : instance(instance), Transparency(instance), lang::Object(instance) {
    InitColorModel();
}

ColorModel::~ColorModel() {}

ColorModel ColorModel::CoerceData(WritableRaster raster, bool isAlphaPremultiplied) {
   return (ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 0), raster.instance, (jboolean)isAlphaPremultiplied));
}

SampleModel ColorModel::CreateCompatibleSampleModel(int w, int h) {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 1), (jint)w, (jint)h));
}

WritableRaster ColorModel::CreateCompatibleWritableRaster(int w, int h) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 2), (jint)w, (jint)h));
}

bool ColorModel::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorModel, 3), obj.instance);
}

void ColorModel::Finalize() {
   tc::jEnv->CallVoidMethod(METHOD(clsColorModel, 4));
}

int ColorModel::GetAlpha(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 5), (jint)pixel);
}

int ColorModel::GetAlpha(lang::Object inData) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 6), inData.instance);
}

WritableRaster ColorModel::GetAlphaRaster(WritableRaster raster) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 7), raster.instance));
}

int ColorModel::GetBlue(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 8), (jint)pixel);
}

int ColorModel::GetBlue(lang::Object inData) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 9), inData.instance);
}

color::ColorSpace ColorModel::GetColorSpace() {
   return (color::ColorSpace)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 10)));
}

int* ColorModel::GetComponents(int* size, int pixel, int* components, int componentsc, int offset) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 11), (jint)pixel, ToJIntArray(components, componentsc), (jint)offset));
}

int* ColorModel::GetComponents(int* size, lang::Object pixel, int* components, int componentsc, int offset) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 12), pixel.instance, ToJIntArray(components, componentsc), (jint)offset));
}

int* ColorModel::GetComponentSize(int* size) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 13)));
}

int ColorModel::GetComponentSize(int componentIdx) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 14), (jint)componentIdx);
}

int ColorModel::GetDataElement(float* normComponents, int normComponentsc, int normOffset) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 15), ToJFloatArray(normComponents, normComponentsc), (jint)normOffset);
}

int ColorModel::GetDataElement(int* components, int componentsc, int offset) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 16), ToJIntArray(components, componentsc), (jint)offset);
}

lang::Object ColorModel::GetDataElements(float* normComponents, int normComponentsc, int normOffset, lang::Object obj) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 17), ToJFloatArray(normComponents, normComponentsc), (jint)normOffset, obj.instance));
}

lang::Object ColorModel::GetDataElements(int* components, int componentsc, int offset, lang::Object obj) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 18), ToJIntArray(components, componentsc), (jint)offset, obj.instance));
}

lang::Object ColorModel::GetDataElements(int rgb, lang::Object pixel) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 19), (jint)rgb, pixel.instance));
}

int ColorModel::GetGreen(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 20), (jint)pixel);
}

int ColorModel::GetGreen(lang::Object inData) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 21), inData.instance);
}

float* ColorModel::GetNormalizedComponents(int* size, int* components, int componentsc, int offset, float* normComponents, int normComponentsc, int normOffset) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 22), ToJIntArray(components, componentsc), (jint)offset, ToJFloatArray(normComponents, normComponentsc), (jint)normOffset));
}

float* ColorModel::GetNormalizedComponents(int* size, lang::Object pixel, float* normComponents, int normComponentsc, int normOffset) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 23), pixel.instance, ToJFloatArray(normComponents, normComponentsc), (jint)normOffset));
}

int ColorModel::GetNumColorComponents() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 24));
}

int ColorModel::GetNumComponents() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 25));
}

int ColorModel::GetPixelSize() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 26));
}

int ColorModel::GetRed(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 27), (jint)pixel);
}

int ColorModel::GetRed(lang::Object inData) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 28), inData.instance);
}

int ColorModel::GetRGB(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 29), (jint)pixel);
}

int ColorModel::GetRGB(lang::Object inData) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 30), inData.instance);
}

ColorModel ColorModel::GetRGBdefault() {
   InitColorModel();
   return (ColorModel)(tc::jEnv->CallStaticObjectMethod(clsColorModel.clazz, clsColorModel.methods[31]));
}

int ColorModel::GetTransferType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 32));
}

int ColorModel::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 33));
}

int* ColorModel::GetUnnormalizedComponents(int* size, float* normComponents, int normComponentsc, int normOffset, int* components, int componentsc, int offset) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 34), ToJFloatArray(normComponents, normComponentsc), (jint)normOffset, ToJIntArray(components, componentsc), (jint)offset));
}

bool ColorModel::HasAlpha() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorModel, 35));
}

int ColorModel::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColorModel, 36));
}

bool ColorModel::IsAlphaPremultiplied() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorModel, 37));
}

bool ColorModel::IsCompatibleRaster(Raster raster) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorModel, 38), raster.instance);
}

bool ColorModel::IsCompatibleSampleModel(SampleModel sm) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColorModel, 39), sm.instance);
}

char* ColorModel::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsColorModel, 40)));
}


// java/awt/image/IndexColorModel
// Name: IndexColorModel
// Base Class(es): [ColorModel]

NativeObject clsIndexColorModel;
jclass InitIndexColorModel() {
   return tcInitNativeObject(clsIndexColorModel, "java/awt/image/IndexColorModel", { 
          { "convertToIntDiscrete", "(Ljava/awt/image/Raster;Z)Ljava/awt/image/BufferedImage;", false }, 
          { "createCompatibleSampleModel", "(II)Ljava/awt/image/SampleModel;", false }, 
          { "createCompatibleWritableRaster", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "finalize", "()V", false }, 
          { "getAlpha", "(I)I", false }, 
          { "getAlphas", "([B)V", false }, 
          { "getBlue", "(I)I", false }, 
          { "getBlues", "([B)V", false }, 
          { "getComponents", "(I[II)[I", false }, 
          { "getComponents", "(Ljava/lang/Object;[II)[I", false }, 
          { "getComponentSize", "()[I", false }, 
          { "getDataElement", "([II)I", false }, 
          { "getDataElements", "([IILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getDataElements", "(ILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "getGreen", "(I)I", false }, 
          { "getGreens", "([B)V", false }, 
          { "getMapSize", "()I", false }, 
          { "getRed", "(I)I", false }, 
          { "getReds", "([B)V", false }, 
          { "getRGB", "(I)I", false }, 
          { "getRGBs", "([I)V", false }, 
          { "getTransparency", "()I", false }, 
          { "getTransparentPixel", "()I", false }, 
          { "getValidPixels", "()Ljava/math/BigInteger;", false }, 
          { "isCompatibleRaster", "(Ljava/awt/image/Raster;)Z", false }, 
          { "isCompatibleSampleModel", "(Ljava/awt/image/SampleModel;)Z", false }, 
          { "isValid", "()Z", false }, 
          { "isValid", "(I)Z", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

IndexColorModel::IndexColorModel(jobject instance)
 : instance(instance), ColorModel(instance) {
    InitIndexColorModel();
}

IndexColorModel::~IndexColorModel() {}

IndexColorModel::IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[B[B[B)V"), (jint)bits, (jint)size, ToJByteArray(r, rc), ToJByteArray(g, gc), ToJByteArray(b, bc))), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc, signed char* a, int ac)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[B[B[B[B)V"), (jint)bits, (jint)size, ToJByteArray(r, rc), ToJByteArray(g, gc), ToJByteArray(b, bc), ToJByteArray(a, ac))), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, signed char* r, int rc, signed char* g, int gc, signed char* b, int bc, int trans)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[B[B[BI)V"), (jint)bits, (jint)size, ToJByteArray(r, rc), ToJByteArray(g, gc), ToJByteArray(b, bc), (jint)trans)), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, signed char* cmap, int cmapc, int start, bool hasalpha)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[BIZ)V"), (jint)bits, (jint)size, ToJByteArray(cmap, cmapc), (jint)start, (jboolean)hasalpha)), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, signed char* cmap, int cmapc, int start, bool hasalpha, int trans)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[BIZI)V"), (jint)bits, (jint)size, ToJByteArray(cmap, cmapc), (jint)start, (jboolean)hasalpha, (jint)trans)), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, int* cmap, int cmapc, int start, bool hasalpha, int trans, int transferType)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[IIZII)V"), (jint)bits, (jint)size, ToJIntArray(cmap, cmapc), (jint)start, (jboolean)hasalpha, (jint)trans, (jint)transferType)), ColorModel(instance)
{}

IndexColorModel::IndexColorModel(int bits, int size, int* cmap, int cmapc, int start, int transferType, math::BigInteger validBits)
 : instance(tc::jEnv->NewObject(InitIndexColorModel(), tc::jEnv->GetMethodID(InitIndexColorModel(), "<init>", "(II[IIILjava/math/BigInteger;)V"), (jint)bits, (jint)size, ToJIntArray(cmap, cmapc), (jint)start, (jint)transferType, validBits.instance)), ColorModel(instance)
{}

BufferedImage IndexColorModel::ConvertToIntDiscrete(Raster raster, bool forceARGB) {
   return (BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 0), raster.instance, (jboolean)forceARGB));
}

SampleModel IndexColorModel::CreateCompatibleSampleModel(int w, int h) {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 1), (jint)w, (jint)h));
}

WritableRaster IndexColorModel::CreateCompatibleWritableRaster(int w, int h) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 2), (jint)w, (jint)h));
}

void IndexColorModel::Finalize() {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 3));
}

int IndexColorModel::GetAlpha(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 4), (jint)pixel);
}

void IndexColorModel::GetAlphas(signed char* a, int ac) {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 5), ToJByteArray(a, ac));
}

int IndexColorModel::GetBlue(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 6), (jint)pixel);
}

void IndexColorModel::GetBlues(signed char* b, int bc) {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 7), ToJByteArray(b, bc));
}

int* IndexColorModel::GetComponents(int* size, int pixel, int* components, int componentsc, int offset) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 8), (jint)pixel, ToJIntArray(components, componentsc), (jint)offset));
}

int* IndexColorModel::GetComponents(int* size, lang::Object pixel, int* components, int componentsc, int offset) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 9), pixel.instance, ToJIntArray(components, componentsc), (jint)offset));
}

int* IndexColorModel::GetComponentSize(int* size) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 10)));
}

int IndexColorModel::GetDataElement(int* components, int componentsc, int offset) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 11), ToJIntArray(components, componentsc), (jint)offset);
}

lang::Object IndexColorModel::GetDataElements(int* components, int componentsc, int offset, lang::Object obj) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 12), ToJIntArray(components, componentsc), (jint)offset, obj.instance));
}

lang::Object IndexColorModel::GetDataElements(int rgb, lang::Object pixel) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 13), (jint)rgb, pixel.instance));
}

int IndexColorModel::GetGreen(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 14), (jint)pixel);
}

void IndexColorModel::GetGreens(signed char* g, int gc) {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 15), ToJByteArray(g, gc));
}

int IndexColorModel::GetMapSize() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 16));
}

int IndexColorModel::GetRed(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 17), (jint)pixel);
}

void IndexColorModel::GetReds(signed char* r, int rc) {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 18), ToJByteArray(r, rc));
}

int IndexColorModel::GetRGB(int pixel) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 19), (jint)pixel);
}

void IndexColorModel::GetRGBs(int* rgb, int rgbc) {
   tc::jEnv->CallVoidMethod(METHOD(clsIndexColorModel, 20), ToJIntArray(rgb, rgbc));
}

int IndexColorModel::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 21));
}

int IndexColorModel::GetTransparentPixel() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsIndexColorModel, 22));
}

math::BigInteger IndexColorModel::GetValidPixels() {
   return (math::BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 23)));
}

bool IndexColorModel::IsCompatibleRaster(Raster raster) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsIndexColorModel, 24), raster.instance);
}

bool IndexColorModel::IsCompatibleSampleModel(SampleModel sm) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsIndexColorModel, 25), sm.instance);
}

bool IndexColorModel::IsValid() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsIndexColorModel, 26));
}

bool IndexColorModel::IsValid(int pixel) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsIndexColorModel, 27), (jint)pixel);
}

char* IndexColorModel::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsIndexColorModel, 28)));
}


// java/awt/image/RenderedImage
// Name: RenderedImage
// Base Class(es): [NativeObjectInstance]

NativeObject clsRenderedImage;
jclass InitRenderedImage() {
   return tcInitNativeObject(clsRenderedImage, "java/awt/image/RenderedImage", { 
          { "copyData", "(Ljava/awt/image/WritableRaster;)Ljava/awt/image/WritableRaster;", false }, 
          { "getColorModel", "()Ljava/awt/image/ColorModel;", false }, 
          { "getData", "()Ljava/awt/image/Raster;", false }, 
          { "getData", "(Ljava/awt/Rectangle;)Ljava/awt/image/Raster;", false }, 
          { "getHeight", "()I", false }, 
          { "getMinTileX", "()I", false }, 
          { "getMinTileY", "()I", false }, 
          { "getMinX", "()I", false }, 
          { "getMinY", "()I", false }, 
          { "getNumXTiles", "()I", false }, 
          { "getNumYTiles", "()I", false }, 
          { "getProperty", "(Ljava/lang/String;)Ljava/lang/Object;", false }, 
          { "getPropertyNames", "()[Ljava/lang/String;", false }, 
          { "getSampleModel", "()Ljava/awt/image/SampleModel;", false }, 
          { "getSources", "()Ljava/util/Vector;", false }, 
          { "getTile", "(II)Ljava/awt/image/Raster;", false }, 
          { "getTileGridXOffset", "()I", false }, 
          { "getTileGridYOffset", "()I", false }, 
          { "getTileHeight", "()I", false }, 
          { "getTileWidth", "()I", false }, 
          { "getWidth", "()I", false }
       }, { 
       });
}

RenderedImage::RenderedImage(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitRenderedImage();
}

RenderedImage::~RenderedImage() {}

WritableRaster RenderedImage::CopyData(WritableRaster raster) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 0), raster.instance));
}

ColorModel RenderedImage::GetColorModel() {
   return (ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 1)));
}

Raster RenderedImage::GetData() {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 2)));
}

Raster RenderedImage::GetData(Rectangle rect) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 3), rect.instance));
}

int RenderedImage::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 4));
}

int RenderedImage::GetMinTileX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 5));
}

int RenderedImage::GetMinTileY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 6));
}

int RenderedImage::GetMinX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 7));
}

int RenderedImage::GetMinY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 8));
}

int RenderedImage::GetNumXTiles() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 9));
}

int RenderedImage::GetNumYTiles() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 10));
}

lang::Object RenderedImage::GetProperty(const char* name) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 11), ToJString(name)));
}

char** RenderedImage::GetPropertyNames(int* size) {
   return FromJStringArray(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 12)));
}

SampleModel RenderedImage::GetSampleModel() {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 13)));
}

util::Vector RenderedImage::GetSources() {
   return (util::Vector)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 14)));
}

Raster RenderedImage::GetTile(int tileX, int tileY) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsRenderedImage, 15), (jint)tileX, (jint)tileY));
}

int RenderedImage::GetTileGridXOffset() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 16));
}

int RenderedImage::GetTileGridYOffset() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 17));
}

int RenderedImage::GetTileHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 18));
}

int RenderedImage::GetTileWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 19));
}

int RenderedImage::GetWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderedImage, 20));
}


// java/awt/image/TileObserver
// Name: TileObserver
// Base Class(es): [NativeObjectInstance]

NativeObject clsTileObserver;
jclass InitTileObserver() {
   return tcInitNativeObject(clsTileObserver, "java/awt/image/TileObserver", { 
          { "tileUpdate", "(Ljava/awt/image/WritableRenderedImage;IIZ)V", false }
       }, { 
       });
}

TileObserver::TileObserver(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitTileObserver();
}

TileObserver::~TileObserver() {}

void TileObserver::TileUpdate(WritableRenderedImage source, int tileX, int tileY, bool willBeWritable) {
   tc::jEnv->CallVoidMethod(METHOD(clsTileObserver, 0), source.instance, (jint)tileX, (jint)tileY, (jboolean)willBeWritable);
}


// java/awt/image/ImageObserver
// Name: ImageObserver
// Base Class(es): [NativeObjectInstance]

NativeObject clsImageObserver;
jclass InitImageObserver() {
   return tcInitNativeObject(clsImageObserver, "java/awt/image/ImageObserver", { 
          { "imageUpdate", "(Ljava/awt/Image;IIIII)Z", false }
       }, { 
       });
}

ImageObserver::ImageObserver(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitImageObserver();
}

ImageObserver::~ImageObserver() {}

bool ImageObserver::ImageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsImageObserver, 0), img.instance, (jint)infoflags, (jint)x, (jint)y, (jint)width, (jint)height);
}


// java/awt/image/ImageConsumer
// Name: ImageConsumer
// Base Class(es): [NativeObjectInstance]

NativeObject clsImageConsumer;
jclass InitImageConsumer() {
   return tcInitNativeObject(clsImageConsumer, "java/awt/image/ImageConsumer", { 
          { "imageComplete", "(I)V", false }, 
          { "setColorModel", "(Ljava/awt/image/ColorModel;)V", false }, 
          { "setDimensions", "(II)V", false }, 
          { "setHints", "(I)V", false }, 
          { "setPixels", "(IIIILjava/awt/image/ColorModel;[BII)V", false }, 
          { "setPixels", "(IIIILjava/awt/image/ColorModel;[III)V", false }, 
          { "setProperties", "(Ljava/util/Hashtable;)V", false }
       }, { 
       });
}

ImageConsumer::ImageConsumer(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitImageConsumer();
}

ImageConsumer::~ImageConsumer() {}

void ImageConsumer::ImageComplete(int status) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 0), (jint)status);
}

void ImageConsumer::SetColorModel(ColorModel model) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 1), model.instance);
}

void ImageConsumer::SetDimensions(int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 2), (jint)width, (jint)height);
}

void ImageConsumer::SetHints(int hitflags) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 3), (jint)hitflags);
}

void ImageConsumer::SetPixels(int x, int y, int w, int h, ColorModel model, signed char* pixels, int pixelsc, int off, int scansize) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 4), (jint)x, (jint)y, (jint)w, (jint)h, model.instance, ToJByteArray(pixels, pixelsc), (jint)off, (jint)scansize);
}

void ImageConsumer::SetPixels(int x, int y, int w, int h, ColorModel model, int* pixels, int pixelsc, int off, int scansize) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 5), (jint)x, (jint)y, (jint)w, (jint)h, model.instance, ToJIntArray(pixels, pixelsc), (jint)off, (jint)scansize);
}

void ImageConsumer::SetProperties(util::Hashtable props) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageConsumer, 6), props.instance);
}


// java/awt/image/ImageProducer
// Name: ImageProducer
// Base Class(es): [NativeObjectInstance]

NativeObject clsImageProducer;
jclass InitImageProducer() {
   return tcInitNativeObject(clsImageProducer, "java/awt/image/ImageProducer", { 
          { "addConsumer", "(Ljava/awt/image/ImageConsumer;)V", false }, 
          { "isConsumer", "(Ljava/awt/image/ImageConsumer;)Z", false }, 
          { "removeConsumer", "(Ljava/awt/image/ImageConsumer;)V", false }, 
          { "requestTopDownLeftRightResend", "(Ljava/awt/image/ImageConsumer;)V", false }, 
          { "startProduction", "(Ljava/awt/image/ImageConsumer;)V", false }
       }, { 
       });
}

ImageProducer::ImageProducer(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitImageProducer();
}

ImageProducer::~ImageProducer() {}

void ImageProducer::AddConsumer(ImageConsumer ic) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageProducer, 0), ic.instance);
}

bool ImageProducer::IsConsumer(ImageConsumer ic) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsImageProducer, 1), ic.instance);
}

void ImageProducer::RemoveConsumer(ImageConsumer ic) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageProducer, 2), ic.instance);
}

void ImageProducer::RequestTopDownLeftRightResend(ImageConsumer ic) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageProducer, 3), ic.instance);
}

void ImageProducer::StartProduction(ImageConsumer ic) {
   tc::jEnv->CallVoidMethod(METHOD(clsImageProducer, 4), ic.instance);
}


// java/awt/image/WritableRenderedImage
// Name: WritableRenderedImage
// Base Class(es): [RenderedImage]

NativeObject clsWritableRenderedImage;
jclass InitWritableRenderedImage() {
   return tcInitNativeObject(clsWritableRenderedImage, "java/awt/image/WritableRenderedImage", { 
          { "addTileObserver", "(Ljava/awt/image/TileObserver;)V", false }, 
          { "getWritableTile", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "getWritableTileIndices", "()[Ljava/awt/Point;", false }, 
          { "hasTileWriters", "()Z", false }, 
          { "isTileWritable", "(II)Z", false }, 
          { "releaseWritableTile", "(II)V", false }, 
          { "removeTileObserver", "(Ljava/awt/image/TileObserver;)V", false }, 
          { "setData", "(Ljava/awt/image/Raster;)V", false }
       }, { 
       });
}

WritableRenderedImage::WritableRenderedImage(jobject instance)
 : instance(instance), RenderedImage(instance) {
    InitWritableRenderedImage();
}

WritableRenderedImage::~WritableRenderedImage() {}

void WritableRenderedImage::AddTileObserver(TileObserver to) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRenderedImage, 0), to.instance);
}

WritableRaster WritableRenderedImage::GetWritableTile(int tileX, int tileY) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsWritableRenderedImage, 1), (jint)tileX, (jint)tileY));
}

Point* WritableRenderedImage::GetWritableTileIndices(int* size) {
   return FromJObjectArray<Point>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsWritableRenderedImage, 2)));
}

bool WritableRenderedImage::HasTileWriters() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsWritableRenderedImage, 3));
}

bool WritableRenderedImage::IsTileWritable(int tileX, int tileY) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsWritableRenderedImage, 4), (jint)tileX, (jint)tileY);
}

void WritableRenderedImage::ReleaseWritableTile(int tileX, int tileY) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRenderedImage, 5), (jint)tileX, (jint)tileY);
}

void WritableRenderedImage::RemoveTileObserver(TileObserver to) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRenderedImage, 6), to.instance);
}

void WritableRenderedImage::SetData(Raster r) {
   tc::jEnv->CallVoidMethod(METHOD(clsWritableRenderedImage, 7), r.instance);
}


// java/awt/ImageCapabilities
// Name: ImageCapabilities
// Base Class(es): [lang::Object]

NativeObject clsImageCapabilities;
jclass InitImageCapabilities() {
   return tcInitNativeObject(clsImageCapabilities, "java/awt/ImageCapabilities", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "isAccelerated", "()Z", false }, 
          { "isTrueVolatile", "()Z", false }
       }, { 
       });
}

ImageCapabilities::ImageCapabilities(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitImageCapabilities();
}

ImageCapabilities::ImageCapabilities(bool accelerated)
 : instance(tc::jEnv->NewObject(InitImageCapabilities(), tc::jEnv->GetMethodID(InitImageCapabilities(), "<init>", "(Z)V"), (jboolean)accelerated)), lang::Object(instance)
{}

ImageCapabilities::~ImageCapabilities() {}

lang::Object ImageCapabilities::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsImageCapabilities, 0)));
}

bool ImageCapabilities::IsAccelerated() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsImageCapabilities, 1));
}

bool ImageCapabilities::IsTrueVolatile() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsImageCapabilities, 2));
}


// java/awt/BufferCapabilities
// Name: BufferCapabilities
// Base Class(es): [lang::Object]

NativeObject clsBufferCapabilities;
jclass InitBufferCapabilities() {
   return tcInitNativeObject(clsBufferCapabilities, "java/awt/BufferCapabilities", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "getBackBufferCapabilities", "()Ljava/awt/ImageCapabilities;", false }, 
          { "getFlipContents", "()Ljava/awt/BufferCapabilities$FlipContents;", false }, 
          { "getFrontBufferCapabilities", "()Ljava/awt/ImageCapabilities;", false }, 
          { "isFullScreenRequired", "()Z", false }, 
          { "isMultiBufferAvailable", "()Z", false }, 
          { "isPageFlipping", "()Z", false }
       }, { 
       });
}

BufferCapabilities::BufferCapabilities(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitBufferCapabilities();
}

BufferCapabilities::~BufferCapabilities() {}

BufferCapabilities::BufferCapabilities(ImageCapabilities frontCaps, ImageCapabilities backCaps, BufferCapabilities::FlipContents flipContents)
 : instance(tc::jEnv->NewObject(InitBufferCapabilities(), tc::jEnv->GetMethodID(InitBufferCapabilities(), "<init>", "(Ljava/awt/ImageCapabilities;Ljava/awt/ImageCapabilities;Ljava/awt/BufferCapabilities$FlipContents;)V"), frontCaps.instance, backCaps.instance, flipContents.instance)), lang::Object(instance)
{}

lang::Object BufferCapabilities::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsBufferCapabilities, 0)));
}

ImageCapabilities BufferCapabilities::GetBackBufferCapabilities() {
   return (ImageCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsBufferCapabilities, 1)));
}

BufferCapabilities::FlipContents BufferCapabilities::GetFlipContents() {
   return (BufferCapabilities::FlipContents)(tc::jEnv->CallObjectMethod(METHOD(clsBufferCapabilities, 2)));
}

ImageCapabilities BufferCapabilities::GetFrontBufferCapabilities() {
   return (ImageCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsBufferCapabilities, 3)));
}

bool BufferCapabilities::IsFullScreenRequired() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferCapabilities, 4));
}

bool BufferCapabilities::IsMultiBufferAvailable() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferCapabilities, 5));
}

bool BufferCapabilities::IsPageFlipping() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferCapabilities, 6));
}


// java/awt/BufferCapabilities/FlipContents
// Name: BufferCapabilities::FlipContents
// Base Class(es): [lang::Object]

NativeObject clsBufferCapabilities_FlipContents;
jclass InitBufferCapabilities_FlipContents() {
   return tcInitNativeObject(clsBufferCapabilities_FlipContents, "java/awt/BufferCapabilities/FlipContents", { 
          { "hashCode", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
          { "BACKGROUND", "Ljava/awt/BufferCapabilities$FlipContents;", true }, 
          { "COPIED", "Ljava/awt/BufferCapabilities$FlipContents;", true }, 
          { "PRIOR", "Ljava/awt/BufferCapabilities$FlipContents;", true }, 
          { "UNDEFINED", "Ljava/awt/BufferCapabilities$FlipContents;", true }
       });
}

BufferCapabilities::FlipContents::FlipContents(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitBufferCapabilities_FlipContents();
}

BufferCapabilities::FlipContents::~FlipContents() {}

BufferCapabilities::FlipContents BufferCapabilities::FlipContents::BACKGROUND() {
   InitBufferCapabilities_FlipContents();
   return (BufferCapabilities::FlipContents)(tc::jEnv->GetStaticObjectField(clsBufferCapabilities_FlipContents.clazz, clsBufferCapabilities_FlipContents.fields[0]));
}

BufferCapabilities::FlipContents BufferCapabilities::FlipContents::COPIED() {
   InitBufferCapabilities_FlipContents();
   return (BufferCapabilities::FlipContents)(tc::jEnv->GetStaticObjectField(clsBufferCapabilities_FlipContents.clazz, clsBufferCapabilities_FlipContents.fields[1]));
}

BufferCapabilities::FlipContents BufferCapabilities::FlipContents::PRIOR() {
   InitBufferCapabilities_FlipContents();
   return (BufferCapabilities::FlipContents)(tc::jEnv->GetStaticObjectField(clsBufferCapabilities_FlipContents.clazz, clsBufferCapabilities_FlipContents.fields[2]));
}

BufferCapabilities::FlipContents BufferCapabilities::FlipContents::UNDEFINED() {
   InitBufferCapabilities_FlipContents();
   return (BufferCapabilities::FlipContents)(tc::jEnv->GetStaticObjectField(clsBufferCapabilities_FlipContents.clazz, clsBufferCapabilities_FlipContents.fields[3]));
}

int BufferCapabilities::FlipContents::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferCapabilities_FlipContents, 0));
}

char* BufferCapabilities::FlipContents::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsBufferCapabilities_FlipContents, 1)));
}


// java/awt/GraphicsConfiguration
// Name: GraphicsConfiguration
// Base Class(es): [lang::Object]

NativeObject clsGraphicsConfiguration;
jclass InitGraphicsConfiguration() {
   return tcInitNativeObject(clsGraphicsConfiguration, "java/awt/GraphicsConfiguration", { 
          { "createCompatibleImage", "(II)Ljava/awt/image/BufferedImage;", false }, 
          { "createCompatibleImage", "(III)Ljava/awt/image/BufferedImage;", false }, 
          { "createCompatibleVolatileImage", "(II)Ljava/awt/image/VolatileImage;", false }, 
          { "createCompatibleVolatileImage", "(IILjava/awt/ImageCapabilities;)Ljava/awt/image/VolatileImage;", false }, 
          { "createCompatibleVolatileImage", "(IILjava/awt/ImageCapabilities;I)Ljava/awt/image/VolatileImage;", false }, 
          { "createCompatibleVolatileImage", "(III)Ljava/awt/image/VolatileImage;", false }, 
          { "getBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getBufferCapabilities", "()Ljava/awt/BufferCapabilities;", false }, 
          { "getColorModel", "()Ljava/awt/image/ColorModel;", false }, 
          { "getColorModel", "(I)Ljava/awt/image/ColorModel;", false }, 
          { "getDefaultTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "getImageCapabilities", "()Ljava/awt/ImageCapabilities;", false }, 
          { "getNormalizingTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "isTranslucencyCapable", "()Z", false }
       }, { 
       });
}

GraphicsConfiguration::GraphicsConfiguration(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitGraphicsConfiguration();
}

GraphicsConfiguration::~GraphicsConfiguration() {}

image::BufferedImage GraphicsConfiguration::CreateCompatibleImage(int width, int height) {
   return (image::BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 0), (jint)width, (jint)height));
}

image::BufferedImage GraphicsConfiguration::CreateCompatibleImage(int width, int height, int transparency) {
   return (image::BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 1), (jint)width, (jint)height, (jint)transparency));
}

image::VolatileImage GraphicsConfiguration::CreateCompatibleVolatileImage(int width, int height) {
   return (image::VolatileImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 2), (jint)width, (jint)height));
}

image::VolatileImage GraphicsConfiguration::CreateCompatibleVolatileImage(int width, int height, ImageCapabilities caps) {
   return (image::VolatileImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 3), (jint)width, (jint)height, caps.instance));
}

image::VolatileImage GraphicsConfiguration::CreateCompatibleVolatileImage(int width, int height, ImageCapabilities caps, int transparency) {
   return (image::VolatileImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 4), (jint)width, (jint)height, caps.instance, (jint)transparency));
}

image::VolatileImage GraphicsConfiguration::CreateCompatibleVolatileImage(int width, int height, int transparency) {
   return (image::VolatileImage)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 5), (jint)width, (jint)height, (jint)transparency));
}

Rectangle GraphicsConfiguration::GetBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 6)));
}

BufferCapabilities GraphicsConfiguration::GetBufferCapabilities() {
   return (BufferCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 7)));
}

image::ColorModel GraphicsConfiguration::GetColorModel() {
   return (image::ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 8)));
}

image::ColorModel GraphicsConfiguration::GetColorModel(int transparency) {
   return (image::ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 9), (jint)transparency));
}

geom::AffineTransform GraphicsConfiguration::GetDefaultTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 10)));
}

ImageCapabilities GraphicsConfiguration::GetImageCapabilities() {
   return (ImageCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 11)));
}

geom::AffineTransform GraphicsConfiguration::GetNormalizingTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsGraphicsConfiguration, 12)));
}

bool GraphicsConfiguration::IsTranslucencyCapable() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphicsConfiguration, 13));
}


// java/awt/Image
// Name: Image
// Base Class(es): [lang::Object]

NativeObject clsImage;
jclass InitImage() {
   return tcInitNativeObject(clsImage, "java/awt/Image", { 
          { "flush", "()V", false }, 
          { "getAccelerationPriority", "()F", false }, 
          { "getCapabilities", "(Ljava/awt/GraphicsConfiguration;)Ljava/awt/ImageCapabilities;", false }, 
          { "getGraphics", "()Ljava/awt/Graphics;", false }, 
          { "getHeight", "(Ljava/awt/image/ImageObserver;)I", false }, 
          { "getScaledInstance", "(III)Ljava/awt/Image;", false }, 
          { "getSource", "()Ljava/awt/image/ImageProducer;", false }, 
          { "getWidth", "(Ljava/awt/image/ImageObserver;)I", false }, 
          { "setAccelerationPriority", "(F)V", false }
       }, { 
       });
}

Image::Image(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitImage();
}

Image::~Image() {}

void Image::Flush() {
   tc::jEnv->CallVoidMethod(METHOD(clsImage, 0));
}

float Image::GetAccelerationPriority() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsImage, 1));
}

ImageCapabilities Image::GetCapabilities(GraphicsConfiguration gc) {
   return (ImageCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsImage, 2), gc.instance));
}

Graphics Image::GetGraphics() {
   return (Graphics)(tc::jEnv->CallObjectMethod(METHOD(clsImage, 3)));
}

int Image::GetHeight(image::ImageObserver observer) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsImage, 4), observer.instance);
}

Image Image::GetScaledInstance(int width, int height, int hints) {
   return (Image)(tc::jEnv->CallObjectMethod(METHOD(clsImage, 5), (jint)width, (jint)height, (jint)hints));
}

image::ImageProducer Image::GetSource() {
   return (image::ImageProducer)(tc::jEnv->CallObjectMethod(METHOD(clsImage, 6)));
}

int Image::GetWidth(image::ImageObserver observer) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsImage, 7), observer.instance);
}

void Image::SetAccelerationPriority(float priority) {
   tc::jEnv->CallVoidMethod(METHOD(clsImage, 8), (jfloat)priority);
}


// java/awt/image/VolatileImage
// Name: VolatileImage
// Base Class(es): [Transparency, Image]

NativeObject clsVolatileImage;
jclass InitVolatileImage() {
   return tcInitNativeObject(clsVolatileImage, "java/awt/image/VolatileImage", { 
          { "contentsLost", "()Z", false }, 
          { "createGraphics", "()Ljava/awt/Graphics2D;", false }, 
          { "getCapabilities", "()Ljava/awt/ImageCapabilities;", false }, 
          { "getGraphics", "()Ljava/awt/Graphics;", false }, 
          { "getHeight", "()I", false }, 
          { "getSnapshot", "()Ljava/awt/image/BufferedImage;", false }, 
          { "getSource", "()Ljava/awt/image/ImageProducer;", false }, 
          { "getTransparency", "()I", false }, 
          { "getWidth", "()I", false }, 
          { "validate", "(Ljava/awt/GraphicsConfiguration;)I", false }
       }, { 
       });
}

VolatileImage::VolatileImage(jobject instance)
 : instance(instance), Transparency(instance), Image(instance) {
    InitVolatileImage();
}

VolatileImage::~VolatileImage() {}

bool VolatileImage::ContentsLost() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVolatileImage, 0));
}

Graphics2D VolatileImage::CreateGraphics() {
   return (Graphics2D)(tc::jEnv->CallObjectMethod(METHOD(clsVolatileImage, 1)));
}

ImageCapabilities VolatileImage::GetCapabilities() {
   return (ImageCapabilities)(tc::jEnv->CallObjectMethod(METHOD(clsVolatileImage, 2)));
}

Graphics VolatileImage::GetGraphics() {
   return (Graphics)(tc::jEnv->CallObjectMethod(METHOD(clsVolatileImage, 3)));
}

int VolatileImage::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVolatileImage, 4));
}

BufferedImage VolatileImage::GetSnapshot() {
   return (BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsVolatileImage, 5)));
}

ImageProducer VolatileImage::GetSource() {
   return (ImageProducer)(tc::jEnv->CallObjectMethod(METHOD(clsVolatileImage, 6)));
}

int VolatileImage::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVolatileImage, 7));
}

int VolatileImage::GetWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVolatileImage, 8));
}

int VolatileImage::Validate(GraphicsConfiguration gc) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVolatileImage, 9), gc.instance);
}


// java/awt/image/BufferedImage
// Name: BufferedImage
// Base Class(es): [Image]

NativeObject clsBufferedImage;
jclass InitBufferedImage() {
   return tcInitNativeObject(clsBufferedImage, "java/awt/image/BufferedImage", { 
          { "addTileObserver", "(Ljava/awt/image/TileObserver;)V", false }, 
          { "coerceData", "(Z)V", false }, 
          { "copyData", "(Ljava/awt/image/WritableRaster;)Ljava/awt/image/WritableRaster;", false }, 
          { "createGraphics", "()Ljava/awt/Graphics2D;", false }, 
          { "getAlphaRaster", "()Ljava/awt/image/WritableRaster;", false }, 
          { "getColorModel", "()Ljava/awt/image/ColorModel;", false }, 
          { "getData", "()Ljava/awt/image/Raster;", false }, 
          { "getData", "(Ljava/awt/Rectangle;)Ljava/awt/image/Raster;", false }, 
          { "getGraphics", "()Ljava/awt/Graphics;", false }, 
          { "getHeight", "()I", false }, 
          { "getHeight", "(Ljava/awt/image/ImageObserver;)I", false }, 
          { "getMinTileX", "()I", false }, 
          { "getMinTileY", "()I", false }, 
          { "getMinX", "()I", false }, 
          { "getMinY", "()I", false }, 
          { "getNumXTiles", "()I", false }, 
          { "getNumYTiles", "()I", false }, 
          { "getProperty", "(Ljava/lang/String;)Ljava/lang/Object;", false }, 
          { "getProperty", "(Ljava/lang/String;Ljava/awt/image/ImageObserver;)Ljava/lang/Object;", false }, 
          { "getPropertyNames", "()[Ljava/lang/String;", false }, 
          { "getRaster", "()Ljava/awt/image/WritableRaster;", false }, 
          { "getRGB", "(II)I", false }, 
          { "getRGB", "(IIII[III)[I", false }, 
          { "getSampleModel", "()Ljava/awt/image/SampleModel;", false }, 
          { "getSource", "()Ljava/awt/image/ImageProducer;", false }, 
          { "getSources", "()Ljava/util/Vector;", false }, 
          { "getSubimage", "(IIII)Ljava/awt/image/BufferedImage;", false }, 
          { "getTile", "(II)Ljava/awt/image/Raster;", false }, 
          { "getTileGridXOffset", "()I", false }, 
          { "getTileGridYOffset", "()I", false }, 
          { "getTileHeight", "()I", false }, 
          { "getTileWidth", "()I", false }, 
          { "getTransparency", "()I", false }, 
          { "getType", "()I", false }, 
          { "getWidth", "()I", false }, 
          { "getWidth", "(Ljava/awt/image/ImageObserver;)I", false }, 
          { "getWritableTile", "(II)Ljava/awt/image/WritableRaster;", false }, 
          { "getWritableTileIndices", "()[Ljava/awt/Point;", false }, 
          { "hasTileWriters", "()Z", false }, 
          { "isAlphaPremultiplied", "()Z", false }, 
          { "isTileWritable", "(II)Z", false }, 
          { "removeTileObserver", "(Ljava/awt/image/TileObserver;)V", false }, 
          { "setData", "(Ljava/awt/image/Raster;)V", false }, 
          { "setRGB", "(III)V", false }, 
          { "setRGB", "(IIII[III)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

BufferedImage::BufferedImage(jobject instance)
 : instance(instance), Image(instance) {
    InitBufferedImage();
}

BufferedImage::~BufferedImage() {}

BufferedImage::BufferedImage(ColorModel cm, WritableRaster raster, bool isRasterPremultiplied, util::Hashtable properties)
 : instance(tc::jEnv->NewObject(InitBufferedImage(), tc::jEnv->GetMethodID(InitBufferedImage(), "<init>", "(Ljava/awt/image/ColorModel;Ljava/awt/image/WritableRaster;ZLjava/util/Hashtable;)V"), cm.instance, raster.instance, (jboolean)isRasterPremultiplied, properties.instance)), Image(instance)
{}

BufferedImage::BufferedImage(int width, int height, int imageType)
 : instance(tc::jEnv->NewObject(InitBufferedImage(), tc::jEnv->GetMethodID(InitBufferedImage(), "<init>", "(III)V"), (jint)width, (jint)height, (jint)imageType)), Image(instance)
{}

BufferedImage::BufferedImage(int width, int height, int imageType, IndexColorModel cm)
 : instance(tc::jEnv->NewObject(InitBufferedImage(), tc::jEnv->GetMethodID(InitBufferedImage(), "<init>", "(IIILjava/awt/image/IndexColorModel;)V"), (jint)width, (jint)height, (jint)imageType, cm.instance)), Image(instance)
{}

void BufferedImage::AddTileObserver(TileObserver to) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 0), to.instance);
}

void BufferedImage::CoerceData(bool isAlphaPremultiplied) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 1), (jboolean)isAlphaPremultiplied);
}

WritableRaster BufferedImage::CopyData(WritableRaster outRaster) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 2), outRaster.instance));
}

Graphics2D BufferedImage::CreateGraphics() {
   return (Graphics2D)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 3)));
}

WritableRaster BufferedImage::GetAlphaRaster() {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 4)));
}

ColorModel BufferedImage::GetColorModel() {
   return (ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 5)));
}

Raster BufferedImage::GetData() {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 6)));
}

Raster BufferedImage::GetData(Rectangle rect) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 7), rect.instance));
}

Graphics BufferedImage::GetGraphics() {
   return (Graphics)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 8)));
}

int BufferedImage::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 9));
}

int BufferedImage::GetHeight(ImageObserver observer) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 10), observer.instance);
}

int BufferedImage::GetMinTileX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 11));
}

int BufferedImage::GetMinTileY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 12));
}

int BufferedImage::GetMinX() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 13));
}

int BufferedImage::GetMinY() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 14));
}

int BufferedImage::GetNumXTiles() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 15));
}

int BufferedImage::GetNumYTiles() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 16));
}

lang::Object BufferedImage::GetProperty(const char* name) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 17), ToJString(name)));
}

lang::Object BufferedImage::GetProperty(const char* name, ImageObserver observer) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 18), ToJString(name), observer.instance));
}

char** BufferedImage::GetPropertyNames(int* size) {
   return FromJStringArray(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 19)));
}

WritableRaster BufferedImage::GetRaster() {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 20)));
}

int BufferedImage::GetRGB(int x, int y) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 21), (jint)x, (jint)y);
}

int* BufferedImage::GetRGB(int* size, int startX, int startY, int w, int h, int* rgbArray, int rgbArrayc, int offset, int scansize) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 22), (jint)startX, (jint)startY, (jint)w, (jint)h, ToJIntArray(rgbArray, rgbArrayc), (jint)offset, (jint)scansize));
}

SampleModel BufferedImage::GetSampleModel() {
   return (SampleModel)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 23)));
}

ImageProducer BufferedImage::GetSource() {
   return (ImageProducer)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 24)));
}

util::Vector BufferedImage::GetSources() {
   return (util::Vector)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 25)));
}

BufferedImage BufferedImage::GetSubimage(int x, int y, int w, int h) {
   return (BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 26), (jint)x, (jint)y, (jint)w, (jint)h));
}

Raster BufferedImage::GetTile(int tileX, int tileY) {
   return (Raster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 27), (jint)tileX, (jint)tileY));
}

int BufferedImage::GetTileGridXOffset() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 28));
}

int BufferedImage::GetTileGridYOffset() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 29));
}

int BufferedImage::GetTileHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 30));
}

int BufferedImage::GetTileWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 31));
}

int BufferedImage::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 32));
}

int BufferedImage::GetType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 33));
}

int BufferedImage::GetWidth() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 34));
}

int BufferedImage::GetWidth(ImageObserver observer) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBufferedImage, 35), observer.instance);
}

WritableRaster BufferedImage::GetWritableTile(int tileX, int tileY) {
   return (WritableRaster)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 36), (jint)tileX, (jint)tileY));
}

Point* BufferedImage::GetWritableTileIndices(int* size) {
   return FromJObjectArray<Point>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 37)));
}

bool BufferedImage::HasTileWriters() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferedImage, 38));
}

bool BufferedImage::IsAlphaPremultiplied() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferedImage, 39));
}

bool BufferedImage::IsTileWritable(int tileX, int tileY) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBufferedImage, 40), (jint)tileX, (jint)tileY);
}

void BufferedImage::RemoveTileObserver(TileObserver to) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 41), to.instance);
}

void BufferedImage::SetData(Raster r) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 42), r.instance);
}

void BufferedImage::SetRGB(int x, int y, int rgb) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 43), (jint)x, (jint)y, (jint)rgb);
}

void BufferedImage::SetRGB(int startX, int startY, int w, int g, int* rgbArray, int rgbArrayc, int offset, int scansize) {
   tc::jEnv->CallVoidMethod(METHOD(clsBufferedImage, 44), (jint)startX, (jint)startY, (jint)w, (jint)g, ToJIntArray(rgbArray, rgbArrayc), (jint)offset, (jint)scansize);
}

char* BufferedImage::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsBufferedImage, 45)));
}


// java/awt/image/BufferedImageOp
// Name: BufferedImageOp
// Base Class(es): [NativeObjectInstance]

NativeObject clsBufferedImageOp;
jclass InitBufferedImageOp() {
   return tcInitNativeObject(clsBufferedImageOp, "java/awt/image/BufferedImageOp", { 
          { "createCompatibleDestImage", "(Ljava/awt/image/BufferedImage;Ljava/awt/image/ColorModel;)Ljava/awt/image/BufferedImage;", false }, 
          { "filter", "(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;", false }, 
          { "getBounds2D", "(Ljava/awt/image/BufferedImage;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getPoint2D", "(Ljava/awt/geom/Point2D;Ljava/awt/geom/Point2D;)Ljava/awt/geom/Point2D;", false }, 
          { "getRenderingHints", "()Ljava/awt/RenderingHints;", false }
       }, { 
       });
}

BufferedImageOp::BufferedImageOp(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitBufferedImageOp();
}

BufferedImageOp::~BufferedImageOp() {}

BufferedImage BufferedImageOp::CreateCompatibleDestImage(BufferedImage src, ColorModel destCM) {
   return (BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImageOp, 0), src.instance, destCM.instance));
}

BufferedImage BufferedImageOp::Filter(BufferedImage src, BufferedImage desc) {
   return (BufferedImage)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImageOp, 1), src.instance, desc.instance));
}

geom::Rectangle2D BufferedImageOp::GetBounds2D(BufferedImage src) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImageOp, 2), src.instance));
}

geom::Point2D BufferedImageOp::GetPoint2D(geom::Point2D srcPt, geom::Point2D dstPt) {
   return (geom::Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImageOp, 3), srcPt.instance, dstPt.instance));
}

RenderingHints BufferedImageOp::GetRenderingHints() {
   return (RenderingHints)(tc::jEnv->CallObjectMethod(METHOD(clsBufferedImageOp, 4)));
}


// java/awt/PaintContext
// Name: PaintContext
// Base Class(es): [NativeObjectInstance]

NativeObject clsPaintContext;
jclass InitPaintContext() {
   return tcInitNativeObject(clsPaintContext, "java/awt/PaintContext", { 
          { "dispose", "()V", false }, 
          { "getColorModel", "()Ljava/awt/image/ColorModel;", false }, 
          { "getRaster", "(IIII)Ljava/awt/image/Raster;", false }
       }, { 
       });
}

PaintContext::PaintContext(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitPaintContext();
}

PaintContext::~PaintContext() {}

void PaintContext::Dispose() {
   tc::jEnv->CallVoidMethod(METHOD(clsPaintContext, 0));
}

image::ColorModel PaintContext::GetColorModel() {
   return (image::ColorModel)(tc::jEnv->CallObjectMethod(METHOD(clsPaintContext, 1)));
}

image::Raster PaintContext::GetRaster(int x, int y, int w, int h) {
   return (image::Raster)(tc::jEnv->CallObjectMethod(METHOD(clsPaintContext, 2), (jint)x, (jint)y, (jint)w, (jint)h));
}


// java/awt/RenderingHints
// Name: RenderingHints
// Base Class(es): [util::Map, lang::Object]

NativeObject clsRenderingHints;
jclass InitRenderingHints() {
   return tcInitNativeObject(clsRenderingHints, "java/awt/RenderingHints", { 
          { "add", "(Ljava/awt/RenderingHints;)V", false }, 
          { "clear", "()V", false }, 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "containsKey", "(Ljava/lang/Object;)Z", false }, 
          { "containsValue", "(Ljava/lang/Object;)Z", false }, 
          { "entrySet", "()Ljava/util/Set;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "isEmpty", "()Z", false }, 
          { "keySet", "()Ljava/util/Set;", false }, 
          { "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "putAll", "(Ljava/util/Map;)V", false }, 
          { "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "size", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "values", "()Ljava/util/Collection;", false }
       }, { 
          { "KEY_ALPHA_INTERPOLATION", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_ANTIALIASING", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_COLOR_RENDERING", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_DITHERING", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_FRACTIONALMETRICS", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_INTERPOLATION", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_RENDERING", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_STROKE_CONTROL", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_TEXT_ANTIALIASING", "Ljava/awt/RenderingHints$Key;", true }, 
          { "KEY_TEXT_LCD_CONTRAST", "Ljava/awt/RenderingHints$Key;", true }, 
          { "VALUE_ALPHA_INTERPOLATION_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_ALPHA_INTERPOLATION_QUALITY", "Ljava/lang/Object;", true }, 
          { "VALUE_ALPHA_INTERPOLATION_SPEED", "Ljava/lang/Object;", true }, 
          { "VALUE_ANTIALIAS_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_ANTIALIAS_OFF", "Ljava/lang/Object;", true }, 
          { "VALUE_ANTIALIAS_ON", "Ljava/lang/Object;", true }, 
          { "VALUE_COLOR_RENDER_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_COLOR_RENDER_QUALITY", "Ljava/lang/Object;", true }, 
          { "VALUE_COLOR_RENDER_SPEED", "Ljava/lang/Object;", true }, 
          { "VALUE_DITHER_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_DITHER_DISABLE", "Ljava/lang/Object;", true }, 
          { "VALUE_DITHER_ENABLE", "Ljava/lang/Object;", true }, 
          { "VALUE_FRACTIONALMETRICS_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_FRACTIONALMETRICS_OFF", "Ljava/lang/Object;", true }, 
          { "VALUE_FRACTIONALMETRICS_ON", "Ljava/lang/Object;", true }, 
          { "VALUE_INTERPOLATION_BICUBIC", "Ljava/lang/Object;", true }, 
          { "VALUE_INTERPOLATION_BILINEAR", "Ljava/lang/Object;", true }, 
          { "VALUE_INTERPOLATION_NEAREST_NEIGHBOR", "Ljava/lang/Object;", true }, 
          { "VALUE_RENDER_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_RENDER_QUALITY", "Ljava/lang/Object;", true }, 
          { "VALUE_RENDER_SPEED", "Ljava/lang/Object;", true }, 
          { "VALUE_STROKE_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_STROKE_NORMALIZE", "Ljava/lang/Object;", true }, 
          { "VALUE_STROKE_PURE", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_DEFAULT", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_GASP", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_LCD_HBGR", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_LCD_HRGB", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_LCD_VBGR", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_LCD_VRGB", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_OFF", "Ljava/lang/Object;", true }, 
          { "VALUE_TEXT_ANTIALIAS_ON", "Ljava/lang/Object;", true }
       });
}

RenderingHints::RenderingHints(jobject instance)
 : instance(instance), util::Map(instance), lang::Object(instance) {
    InitRenderingHints();
}

RenderingHints::~RenderingHints() {}

RenderingHints::RenderingHints(Map init)
 : instance(tc::jEnv->NewObject(InitRenderingHints(), tc::jEnv->GetMethodID(InitRenderingHints(), "<init>", "(Ljava/util/Map;)V"), init.instance)), util::Map(instance), lang::Object(instance)
{}

RenderingHints::RenderingHints(RenderingHints::Key key, lang::Object value)
 : instance(tc::jEnv->NewObject(InitRenderingHints(), tc::jEnv->GetMethodID(InitRenderingHints(), "<init>", "(Ljava/awt/RenderingHints$Key;Ljava/lang/Object;)V"), key.instance, value.instance)), util::Map(instance), lang::Object(instance)
{}

void RenderingHints::Add(RenderingHints hints) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderingHints, 0), hints.instance);
}

void RenderingHints::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderingHints, 1));
}

lang::Object RenderingHints::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 2)));
}

bool RenderingHints::ContainsKey(lang::Object key) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints, 3), key.instance);
}

bool RenderingHints::ContainsValue(lang::Object value) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints, 4), value.instance);
}

util::Set RenderingHints::EntrySet() {
   return (util::Set)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 5)));
}

bool RenderingHints::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints, 6), o.instance);
}

lang::Object RenderingHints::Get(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 7), key.instance));
}

int RenderingHints::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderingHints, 8));
}

bool RenderingHints::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints, 9));
}

util::Set RenderingHints::KeySet() {
   return (util::Set)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 10)));
}

lang::Object RenderingHints::Put(lang::Object key, lang::Object value) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 11), key.instance, value.instance));
}

void RenderingHints::PutAll(Map m) {
   tc::jEnv->CallVoidMethod(METHOD(clsRenderingHints, 12), m.instance);
}

lang::Object RenderingHints::Remove(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 13), key.instance));
}

int RenderingHints::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderingHints, 14));
}

char* RenderingHints::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 15)));
}

util::Collection RenderingHints::Values() {
   return (util::Collection)(tc::jEnv->CallObjectMethod(METHOD(clsRenderingHints, 16)));
}

RenderingHints::Key RenderingHints::KEY_ALPHA_INTERPOLATION() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[0]));
}

RenderingHints::Key RenderingHints::KEY_ANTIALIASING() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[1]));
}

RenderingHints::Key RenderingHints::KEY_COLOR_RENDERING() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[2]));
}

RenderingHints::Key RenderingHints::KEY_DITHERING() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[3]));
}

RenderingHints::Key RenderingHints::KEY_FRACTIONALMETRICS() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[4]));
}

RenderingHints::Key RenderingHints::KEY_INTERPOLATION() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[5]));
}

RenderingHints::Key RenderingHints::KEY_RENDERING() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[6]));
}

RenderingHints::Key RenderingHints::KEY_STROKE_CONTROL() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[7]));
}

RenderingHints::Key RenderingHints::KEY_TEXT_ANTIALIASING() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[8]));
}

RenderingHints::Key RenderingHints::KEY_TEXT_LCD_CONTRAST() {
   InitRenderingHints();
   return (RenderingHints::Key)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[9]));
}

lang::Object RenderingHints::VALUE_ALPHA_INTERPOLATION_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[10]));
}

lang::Object RenderingHints::VALUE_ALPHA_INTERPOLATION_QUALITY() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[11]));
}

lang::Object RenderingHints::VALUE_ALPHA_INTERPOLATION_SPEED() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[12]));
}

lang::Object RenderingHints::VALUE_ANTIALIAS_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[13]));
}

lang::Object RenderingHints::VALUE_ANTIALIAS_OFF() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[14]));
}

lang::Object RenderingHints::VALUE_ANTIALIAS_ON() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[15]));
}

lang::Object RenderingHints::VALUE_COLOR_RENDER_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[16]));
}

lang::Object RenderingHints::VALUE_COLOR_RENDER_QUALITY() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[17]));
}

lang::Object RenderingHints::VALUE_COLOR_RENDER_SPEED() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[18]));
}

lang::Object RenderingHints::VALUE_DITHER_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[19]));
}

lang::Object RenderingHints::VALUE_DITHER_DISABLE() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[20]));
}

lang::Object RenderingHints::VALUE_DITHER_ENABLE() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[21]));
}

lang::Object RenderingHints::VALUE_FRACTIONALMETRICS_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[22]));
}

lang::Object RenderingHints::VALUE_FRACTIONALMETRICS_OFF() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[23]));
}

lang::Object RenderingHints::VALUE_FRACTIONALMETRICS_ON() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[24]));
}

lang::Object RenderingHints::VALUE_INTERPOLATION_BICUBIC() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[25]));
}

lang::Object RenderingHints::VALUE_INTERPOLATION_BILINEAR() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[26]));
}

lang::Object RenderingHints::VALUE_INTERPOLATION_NEAREST_NEIGHBOR() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[27]));
}

lang::Object RenderingHints::VALUE_RENDER_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[28]));
}

lang::Object RenderingHints::VALUE_RENDER_QUALITY() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[29]));
}

lang::Object RenderingHints::VALUE_RENDER_SPEED() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[30]));
}

lang::Object RenderingHints::VALUE_STROKE_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[31]));
}

lang::Object RenderingHints::VALUE_STROKE_NORMALIZE() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[32]));
}

lang::Object RenderingHints::VALUE_STROKE_PURE() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[33]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_DEFAULT() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[34]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_GASP() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[35]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_LCD_HBGR() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[36]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_LCD_HRGB() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[37]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_LCD_VBGR() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[38]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_LCD_VRGB() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[39]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_OFF() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[40]));
}

lang::Object RenderingHints::VALUE_TEXT_ANTIALIAS_ON() {
   InitRenderingHints();
   return (lang::Object)(tc::jEnv->GetStaticObjectField(clsRenderingHints.clazz, clsRenderingHints.fields[41]));
}


// java/awt/RenderingHints/Key
// Name: RenderingHints::Key
// Base Class(es): [lang::Object]

NativeObject clsRenderingHints_Key;
jclass InitRenderingHints_Key() {
   return tcInitNativeObject(clsRenderingHints_Key, "java/awt/RenderingHints/Key", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "isCompatibleValue", "(Ljava/lang/Object;)Z", false }
       }, { 
       });
}

RenderingHints::Key::Key(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitRenderingHints_Key();
}

RenderingHints::Key::~Key() {}

bool RenderingHints::Key::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints_Key, 0), o.instance);
}

int RenderingHints::Key::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRenderingHints_Key, 1));
}

bool RenderingHints::Key::IsCompatibleValue(lang::Object val) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRenderingHints_Key, 2), val.instance);
}


// java/awt/Paint
// Name: Paint
// Base Class(es): [Transparency]

NativeObject clsPaint;
jclass InitPaint() {
   return tcInitNativeObject(clsPaint, "java/awt/Paint", { 
          { "createContext", "(Ljava/awt/image/ColorModel;Ljava/awt/Rectangle;Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/AffineTransform;Ljava/awt/RenderingHints;)Ljava/awt/PaintContext;", false }
       }, { 
       });
}

Paint::Paint(jobject instance)
 : instance(instance), Transparency(instance) {
    InitPaint();
}

Paint::~Paint() {}

PaintContext Paint::CreateContext(image::ColorModel cm, Rectangle deviceBounds, geom::Rectangle2D userBounds, geom::AffineTransform xform, RenderingHints hints) {
   return (PaintContext)(tc::jEnv->CallObjectMethod(METHOD(clsPaint, 0), cm.instance, deviceBounds.instance, userBounds.instance, xform.instance, hints.instance));
}


// java/awt/Color
// Name: Color
// Base Class(es): [Paint, lang::Object]

NativeObject clsColor;
jclass InitColor() {
   return tcInitNativeObject(clsColor, "java/awt/Color", { 
          { "brighter", "()Ljava/awt/Color;", false }, 
          { "createContext", "(Ljava/awt/image/ColorModel;Ljava/awt/Rectangle;Ljava/awt/geom/Rectangle2D;Ljava/awt/geom/AffineTransform;Ljava/awt/RenderingHints;)Ljava/awt/PaintContext;", false }, 
          { "darker", "()Ljava/awt/Color;", false }, 
          { "decode", "(Ljava/lang/String;)Ljava/awt/Color;", true }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getAlpha", "()I", false }, 
          { "getBlue", "()I", false }, 
          { "getColor", "(Ljava/lang/String;)Ljava/awt/Color;", true }, 
          { "getColor", "(Ljava/lang/String;Ljava/awt/Color;)Ljava/awt/Color;", true }, 
          { "getColor", "(Ljava/lang/String;I)Ljava/awt/Color;", true }, 
          { "getColorComponents", "(Ljava/awt/color/ColorSpace;[F)[F", false }, 
          { "getColorComponents", "([F)[F", false }, 
          { "getColorSpace", "()Ljava/awt/color/ColorSpace;", false }, 
          { "getComponents", "(Ljava/awt/color/ColorSpace;[F)[F", false }, 
          { "getComponents", "([F)[F", false }, 
          { "getGreen", "()I", false }, 
          { "getHSBColor", "(FFF)Ljava/awt/Color;", true }, 
          { "getRed", "()I", false }, 
          { "getRGB", "()I", false }, 
          { "getRGBColorComponents", "([F)[F", false }, 
          { "getRGBComponents", "([F)[F", false }, 
          { "getTransparency", "()I", false }, 
          { "hashCode", "()I", false }, 
          { "HSBtoRGB", "(FFF)I", true }, 
          { "RGBtoHSB", "(III[F)[F", true }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
          { "black", "Ljava/awt/Color;", true }, 
          { "blue", "Ljava/awt/Color;", true }, 
          { "cyan", "Ljava/awt/Color;", true }, 
          { "darkGray", "Ljava/awt/Color;", true }, 
          { "gray", "Ljava/awt/Color;", true }, 
          { "green", "Ljava/awt/Color;", true }, 
          { "lightGray", "Ljava/awt/Color;", true }, 
          { "magenta", "Ljava/awt/Color;", true }, 
          { "orange", "Ljava/awt/Color;", true }, 
          { "pink", "Ljava/awt/Color;", true }, 
          { "red", "Ljava/awt/Color;", true }, 
          { "white", "Ljava/awt/Color;", true }, 
          { "yellow", "Ljava/awt/Color;", true }
       });
}

Color::Color(jobject instance)
 : instance(instance), Paint(instance), lang::Object(instance) {
    InitColor();
}

Color::~Color() {}

Color::Color(color::ColorSpace cspace, float* components, int componentsc, float alpha)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(Ljava/awt/color/ColorSpace;[FF)V"), cspace.instance, ToJFloatArray(components, componentsc), (jfloat)alpha)), Paint(instance), lang::Object(instance)
{}

Color::Color(float r, float g, float b)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(FFF)V"), (jfloat)r, (jfloat)g, (jfloat)b)), Paint(instance), lang::Object(instance)
{}

Color::Color(float r, float g, float b, float a)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(FFFF)V"), (jfloat)r, (jfloat)g, (jfloat)b, (jfloat)a)), Paint(instance), lang::Object(instance)
{}

Color::Color(int rgb)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(I)V"), (jint)rgb)), Paint(instance), lang::Object(instance)
{}

Color::Color(int rgba, bool hasalpha)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(IZ)V"), (jint)rgba, (jboolean)hasalpha)), Paint(instance), lang::Object(instance)
{}

Color::Color(int r, int g, int b)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(III)V"), (jint)r, (jint)g, (jint)b)), Paint(instance), lang::Object(instance)
{}

Color::Color(int r, int g, int b, int a)
 : instance(tc::jEnv->NewObject(InitColor(), tc::jEnv->GetMethodID(InitColor(), "<init>", "(IIII)V"), (jint)r, (jint)g, (jint)b, (jint)a)), Paint(instance), lang::Object(instance)
{}

Color Color::Brighter() {
   return (Color)(tc::jEnv->CallObjectMethod(METHOD(clsColor, 0)));
}

PaintContext Color::CreateContext(image::ColorModel cm, Rectangle r, geom::Rectangle2D r2d, geom::AffineTransform xform, RenderingHints hints) {
   return (PaintContext)(tc::jEnv->CallObjectMethod(METHOD(clsColor, 1), cm.instance, r.instance, r2d.instance, xform.instance, hints.instance));
}

Color Color::Darker() {
   return (Color)(tc::jEnv->CallObjectMethod(METHOD(clsColor, 2)));
}

Color Color::Decode(const char* nm) {
   InitColor();
   return (Color)(tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[3], ToJString(nm)));
}

bool Color::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsColor, 4), obj.instance);
}

int Color::GetAlpha() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 5));
}

int Color::GetBlue() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 6));
}

Color Color::GetColor(const char* nm) {
   InitColor();
   return (Color)(tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[7], ToJString(nm)));
}

Color Color::GetColor(const char* nm, Color v) {
   InitColor();
   return (Color)(tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[8], ToJString(nm), v.instance));
}

Color Color::GetColor(const char* nm, int v) {
   InitColor();
   return (Color)(tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[9], ToJString(nm), (jint)v));
}

float* Color::GetColorComponents(int* size, color::ColorSpace cspace, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 10), cspace.instance, ToJFloatArray(compArray, compArrayc)));
}

float* Color::GetColorComponents(int* size, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 11), ToJFloatArray(compArray, compArrayc)));
}

color::ColorSpace Color::GetColorSpace() {
   return (color::ColorSpace)(tc::jEnv->CallObjectMethod(METHOD(clsColor, 12)));
}

float* Color::GetComponents(int* size, color::ColorSpace cspace, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 13), cspace.instance, ToJFloatArray(compArray, compArrayc)));
}

float* Color::GetComponents(int* size, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 14), ToJFloatArray(compArray, compArrayc)));
}

int Color::GetGreen() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 15));
}

Color Color::GetHSBColor(float h, float s, float b) {
   InitColor();
   return (Color)(tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[16], (jfloat)h, (jfloat)s, (jfloat)b));
}

int Color::GetRed() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 17));
}

int Color::GetRGB() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 18));
}

float* Color::GetRGBColorComponents(int* size, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 19), ToJFloatArray(compArray, compArrayc)));
}

float* Color::GetRGBComponents(int* size, float* compArray, int compArrayc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsColor, 20), ToJFloatArray(compArray, compArrayc)));
}

int Color::GetTransparency() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 21));
}

int Color::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsColor, 22));
}

int Color::HSBtoRGB(float hue, float saturation, float brightness) {
   InitColor();
   return (int)tc::jEnv->CallStaticIntMethod(clsColor.clazz, clsColor.methods[23], (jfloat)hue, (jfloat)saturation, (jfloat)brightness);
}

float* Color::RGBtoHSB(int* size, int r, int g, int b, float* hsbvals, int hsbvalsc) {
   InitColor();
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallStaticObjectMethod(clsColor.clazz, clsColor.methods[24], (jint)r, (jint)g, (jint)b, ToJFloatArray(hsbvals, hsbvalsc)));
}

char* Color::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsColor, 25)));
}

Color Color::Black() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[0]));
}

Color Color::Blue() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[1]));
}

Color Color::Cyan() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[2]));
}

Color Color::DarkGray() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[3]));
}

Color Color::Gray() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[4]));
}

Color Color::Green() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[5]));
}

Color Color::LightGray() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[6]));
}

Color Color::Magenta() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[7]));
}

Color Color::Orange() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[8]));
}

Color Color::Pink() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[9]));
}

Color Color::Red() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[10]));
}

Color Color::White() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[11]));
}

Color Color::Yellow() {
   InitColor();
   return (Color)(tc::jEnv->GetStaticObjectField(clsColor.clazz, clsColor.fields[12]));
}


// java/awt/Stroke
// Name: Stroke
// Base Class(es): [NativeObjectInstance]

NativeObject clsStroke;
jclass InitStroke() {
   return tcInitNativeObject(clsStroke, "java/awt/Stroke", { 
          { "createStrokedShape", "(Ljava/awt/Shape;)Ljava/awt/Shape;", false }
       }, { 
       });
}

Stroke::Stroke(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitStroke();
}

Stroke::~Stroke() {}

Shape Stroke::CreateStrokedShape(Shape p) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsStroke, 0), p.instance));
}


// java/awt/CompositeContext
// Name: CompositeContext
// Base Class(es): [NativeObjectInstance]

NativeObject clsCompositeContext;
jclass InitCompositeContext() {
   return tcInitNativeObject(clsCompositeContext, "java/awt/CompositeContext", { 
          { "compose", "(Ljava/awt/image/Raster;Ljava/awt/image/Raster;Ljava/awt/image/WritableRaster;)V", false }, 
          { "dispose", "()V", false }
       }, { 
       });
}

CompositeContext::CompositeContext(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitCompositeContext();
}

CompositeContext::~CompositeContext() {}

void CompositeContext::Compose(image::Raster src, image::Raster dstIn, image::WritableRaster dstOut) {
   tc::jEnv->CallVoidMethod(METHOD(clsCompositeContext, 0), src.instance, dstIn.instance, dstOut.instance);
}

void CompositeContext::Dispose() {
   tc::jEnv->CallVoidMethod(METHOD(clsCompositeContext, 1));
}


// java/awt/Composite
// Name: Composite
// Base Class(es): [NativeObjectInstance]

NativeObject clsComposite;
jclass InitComposite() {
   return tcInitNativeObject(clsComposite, "java/awt/Composite", { 
          { "createContext", "(Ljava/awt/image/ColorModel;Ljava/awt/image/ColorModel;Ljava/awt/RenderingHints;)Ljava/awt/CompositeContext;", false }
       }, { 
       });
}

Composite::Composite(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitComposite();
}

Composite::~Composite() {}

CompositeContext Composite::CreateContext(image::ColorModel srcColorModel, image::ColorModel dstColorModel, RenderingHints hints) {
   return (CompositeContext)(tc::jEnv->CallObjectMethod(METHOD(clsComposite, 0), srcColorModel.instance, dstColorModel.instance, hints.instance));
}


// java/awt/font/GlyphJustificationInfo
// Name: GlyphJustificationInfo
// Base Class(es): [lang::Object]

NativeObject clsGlyphJustificationInfo;
jclass InitGlyphJustificationInfo() {
   return tcInitNativeObject(clsGlyphJustificationInfo, "java/awt/font/GlyphJustificationInfo", { 
       }, { 
          { "growAbsorb", "Z", false }, 
          { "growLeftLimit", "F", false }, 
          { "growPriority", "I", false }, 
          { "growRightLimit", "F", false }, 
          { "shrinkAbsorb", "Z", false }, 
          { "shrinkLeftLimit", "F", false }, 
          { "shrinkPriority", "I", false }, 
          { "shrinkRightLimit", "F", false }, 
          { "weight", "F", false }
       });
}

GlyphJustificationInfo::GlyphJustificationInfo(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitGlyphJustificationInfo();
}

GlyphJustificationInfo::~GlyphJustificationInfo() {}

GlyphJustificationInfo::GlyphJustificationInfo(float weight, bool growAbsorb, int growPriority, float growLeftLimit, float growRightLimit, bool shrinkAbsorb, int shrinkPriority, float shrinkLeftLimit, float shrinkRightLimit)
 : instance(tc::jEnv->NewObject(InitGlyphJustificationInfo(), tc::jEnv->GetMethodID(InitGlyphJustificationInfo(), "<init>", "(FZIFFZIFF)V"), (jfloat)weight, (jboolean)growAbsorb, (jint)growPriority, (jfloat)growLeftLimit, (jfloat)growRightLimit, (jboolean)shrinkAbsorb, (jint)shrinkPriority, (jfloat)shrinkLeftLimit, (jfloat)shrinkRightLimit)), lang::Object(instance)
{}

bool GlyphJustificationInfo::GrowAbsorb() {
   return (bool)tc::jEnv->GetBooleanField(FIELD(clsGlyphJustificationInfo, 0));
}

float GlyphJustificationInfo::GrowLeftLimit() {
   return (float)tc::jEnv->GetFloatField(FIELD(clsGlyphJustificationInfo, 1));
}

int GlyphJustificationInfo::GrowPriority() {
   return (int)tc::jEnv->GetIntField(FIELD(clsGlyphJustificationInfo, 2));
}

float GlyphJustificationInfo::GrowRightLimit() {
   return (float)tc::jEnv->GetFloatField(FIELD(clsGlyphJustificationInfo, 3));
}

bool GlyphJustificationInfo::ShrinkAbsorb() {
   return (bool)tc::jEnv->GetBooleanField(FIELD(clsGlyphJustificationInfo, 4));
}

float GlyphJustificationInfo::ShrinkLeftLimit() {
   return (float)tc::jEnv->GetFloatField(FIELD(clsGlyphJustificationInfo, 5));
}

int GlyphJustificationInfo::ShrinkPriority() {
   return (int)tc::jEnv->GetIntField(FIELD(clsGlyphJustificationInfo, 6));
}

float GlyphJustificationInfo::ShrinkRightLimit() {
   return (float)tc::jEnv->GetFloatField(FIELD(clsGlyphJustificationInfo, 7));
}

float GlyphJustificationInfo::Weight() {
   return (float)tc::jEnv->GetFloatField(FIELD(clsGlyphJustificationInfo, 8));
}


// java/awt/font/FontRenderContext
// Name: FontRenderContext
// Base Class(es): [lang::Object]

NativeObject clsFontRenderContext;
jclass InitFontRenderContext() {
   return tcInitNativeObject(clsFontRenderContext, "java/awt/font/FontRenderContext", { 
          { "equals", "(Ljava/awt/font/FontRenderContext;)Z", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getAntiAliasingHint", "()Ljava/lang/Object;", false }, 
          { "getFractionalMetricsHint", "()Ljava/lang/Object;", false }, 
          { "getTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "getTransformType", "()I", false }, 
          { "hashCode", "()I", false }, 
          { "isAntiAliased", "()Z", false }, 
          { "isTransformed", "()Z", false }, 
          { "usesFractionalMetrics", "()Z", false }
       }, { 
       });
}

FontRenderContext::FontRenderContext(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitFontRenderContext();
}

FontRenderContext::~FontRenderContext() {}

FontRenderContext::FontRenderContext(geom::AffineTransform tx, bool isAntiAliased, bool usesFractionalMetrics)
 : instance(tc::jEnv->NewObject(InitFontRenderContext(), tc::jEnv->GetMethodID(InitFontRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;ZZ)V"), tx.instance, (jboolean)isAntiAliased, (jboolean)usesFractionalMetrics)), lang::Object(instance)
{}

FontRenderContext::FontRenderContext(geom::AffineTransform tx, lang::Object aaHint, lang::Object fmHint)
 : instance(tc::jEnv->NewObject(InitFontRenderContext(), tc::jEnv->GetMethodID(InitFontRenderContext(), "<init>", "(Ljava/awt/geom/AffineTransform;Ljava/lang/Object;Ljava/lang/Object;)V"), tx.instance, aaHint.instance, fmHint.instance)), lang::Object(instance)
{}

bool FontRenderContext::Equals(FontRenderContext rhs) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontRenderContext, 0), rhs.instance);
}

bool FontRenderContext::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontRenderContext, 1), obj.instance);
}

lang::Object FontRenderContext::GetAntiAliasingHint() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsFontRenderContext, 2)));
}

lang::Object FontRenderContext::GetFractionalMetricsHint() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsFontRenderContext, 3)));
}

geom::AffineTransform FontRenderContext::GetTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsFontRenderContext, 4)));
}

int FontRenderContext::GetTransformType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontRenderContext, 5));
}

int FontRenderContext::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontRenderContext, 6));
}

bool FontRenderContext::IsAntiAliased() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontRenderContext, 7));
}

bool FontRenderContext::IsTransformed() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontRenderContext, 8));
}

bool FontRenderContext::UsesFractionalMetrics() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontRenderContext, 9));
}


// java/awt/font/LineMetrics
// Name: LineMetrics
// Base Class(es): [lang::Object]

NativeObject clsLineMetrics;
jclass InitLineMetrics() {
   return tcInitNativeObject(clsLineMetrics, "java/awt/font/LineMetrics", { 
          { "getAscent", "()F", false }, 
          { "getBaselineIndex", "()I", false }, 
          { "getBaselineOffsets", "()[F", false }, 
          { "getDescent", "()F", false }, 
          { "getHeight", "()F", false }, 
          { "getLeading", "()F", false }, 
          { "getNumChars", "()I", false }, 
          { "getStrikethroughOffset", "()F", false }, 
          { "getStrikethroughThickness", "()F", false }, 
          { "getUnderlineOffset", "()F", false }, 
          { "getUnderlineThickness", "()F", false }
       }, { 
       });
}

LineMetrics::LineMetrics(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitLineMetrics();
}

LineMetrics::~LineMetrics() {}

float LineMetrics::GetAscent() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 0));
}

int LineMetrics::GetBaselineIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsLineMetrics, 1));
}

float* LineMetrics::GetBaselineOffsets(int* size) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsLineMetrics, 2)));
}

float LineMetrics::GetDescent() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 3));
}

float LineMetrics::GetHeight() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 4));
}

float LineMetrics::GetLeading() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 5));
}

int LineMetrics::GetNumChars() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsLineMetrics, 6));
}

float LineMetrics::GetStrikethroughOffset() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 7));
}

float LineMetrics::GetStrikethroughThickness() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 8));
}

float LineMetrics::GetUnderlineOffset() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 9));
}

float LineMetrics::GetUnderlineThickness() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsLineMetrics, 10));
}


// java/awt/font/GlyphMetrics
// Name: GlyphMetrics
// Base Class(es): [lang::Object]

NativeObject clsGlyphMetrics;
jclass InitGlyphMetrics() {
   return tcInitNativeObject(clsGlyphMetrics, "java/awt/font/GlyphMetrics", { 
          { "getAdvance", "()F", false }, 
          { "getAdvanceX", "()F", false }, 
          { "getAdvanceY", "()F", false }, 
          { "getBounds2D", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getLSB", "()F", false }, 
          { "getRSB", "()F", false }, 
          { "getType", "()I", false }, 
          { "isCombining", "()Z", false }, 
          { "isComponent", "()Z", false }, 
          { "isLigature", "()Z", false }, 
          { "isStandard", "()Z", false }, 
          { "isWhitespace", "()Z", false }
       }, { 
       });
}

GlyphMetrics::GlyphMetrics(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitGlyphMetrics();
}

GlyphMetrics::~GlyphMetrics() {}

GlyphMetrics::GlyphMetrics(bool horizontal, float advanceX, float advanceY, geom::Rectangle2D bounds, signed char glyphType)
 : instance(tc::jEnv->NewObject(InitGlyphMetrics(), tc::jEnv->GetMethodID(InitGlyphMetrics(), "<init>", "(ZFFLjava/awt/geom/Rectangle2D;B)V"), (jboolean)horizontal, (jfloat)advanceX, (jfloat)advanceY, bounds.instance, (jbyte)glyphType)), lang::Object(instance)
{}

GlyphMetrics::GlyphMetrics(float advance, geom::Rectangle2D bounds, signed char glyphType)
 : instance(tc::jEnv->NewObject(InitGlyphMetrics(), tc::jEnv->GetMethodID(InitGlyphMetrics(), "<init>", "(FLjava/awt/geom/Rectangle2D;B)V"), (jfloat)advance, bounds.instance, (jbyte)glyphType)), lang::Object(instance)
{}

float GlyphMetrics::GetAdvance() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsGlyphMetrics, 0));
}

float GlyphMetrics::GetAdvanceX() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsGlyphMetrics, 1));
}

float GlyphMetrics::GetAdvanceY() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsGlyphMetrics, 2));
}

geom::Rectangle2D GlyphMetrics::GetBounds2D() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphMetrics, 3)));
}

float GlyphMetrics::GetLSB() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsGlyphMetrics, 4));
}

float GlyphMetrics::GetRSB() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsGlyphMetrics, 5));
}

int GlyphMetrics::GetType() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsGlyphMetrics, 6));
}

bool GlyphMetrics::IsCombining() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphMetrics, 7));
}

bool GlyphMetrics::IsComponent() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphMetrics, 8));
}

bool GlyphMetrics::IsLigature() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphMetrics, 9));
}

bool GlyphMetrics::IsStandard() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphMetrics, 10));
}

bool GlyphMetrics::IsWhitespace() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphMetrics, 11));
}


// java/awt/font/GlyphVector
// Name: GlyphVector
// Base Class(es): [lang::Object]

NativeObject clsGlyphVector;
jclass InitGlyphVector() {
   return tcInitNativeObject(clsGlyphVector, "java/awt/font/GlyphVector", { 
          { "equals", "(Ljava/awt/font/GlyphVector;)Z", false }, 
          { "getFont", "()Ljava/awt/Font;", false }, 
          { "getFontRenderContext", "()Ljava/awt/font/FontRenderContext;", false }, 
          { "getGlyphCharIndex", "(I)I", false }, 
          { "getGlyphCharIndices", "(II[I)[I", false }, 
          { "getGlyphCode", "(I)I", false }, 
          { "getGlyphCodes", "(II[I)[I", false }, 
          { "getGlyphJustificationInfo", "(I)Ljava/awt/font/GlyphJustificationInfo;", false }, 
          { "getGlyphLogicalBounds", "(I)Ljava/awt/Shape;", false }, 
          { "getGlyphMetrics", "(I)Ljava/awt/font/GlyphMetrics;", false }, 
          { "getGlyphOutline", "(I)Ljava/awt/Shape;", false }, 
          { "getGlyphOutline", "(IFF)Ljava/awt/Shape;", false }, 
          { "getGlyphPixelBounds", "(ILjava/awt/font/FontRenderContext;FF)Ljava/awt/Rectangle;", false }, 
          { "getGlyphPosition", "(I)Ljava/awt/geom/Point2D;", false }, 
          { "getGlyphPositions", "(II[F)[F", false }, 
          { "getGlyphTransform", "(I)Ljava/awt/geom/AffineTransform;", false }, 
          { "getGlyphVisualBounds", "(I)Ljava/awt/Shape;", false }, 
          { "getLayoutFlags", "()I", false }, 
          { "getLogicalBounds", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "getNumGlyphs", "()I", false }, 
          { "getOutline", "()Ljava/awt/Shape;", false }, 
          { "getOutline", "(FF)Ljava/awt/Shape;", false }, 
          { "getPixelBounds", "(Ljava/awt/font/FontRenderContext;FF)Ljava/awt/Rectangle;", false }, 
          { "getVisualBounds", "()Ljava/awt/geom/Rectangle2D;", false }, 
          { "performDefaultLayout", "()V", false }, 
          { "setGlyphPosition", "(ILjava/awt/geom/Point2D;)V", false }, 
          { "setGlyphTransform", "(ILjava/awt/geom/AffineTransform;)V", false }
       }, { 
       });
}

GlyphVector::GlyphVector(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitGlyphVector();
}

GlyphVector::~GlyphVector() {}

bool GlyphVector::Equals(GlyphVector set) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGlyphVector, 0), set.instance);
}

Font GlyphVector::GetFont() {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 1)));
}

FontRenderContext GlyphVector::GetFontRenderContext() {
   return (FontRenderContext)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 2)));
}

int GlyphVector::GetGlyphCharIndex(int glyphIndex) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsGlyphVector, 3), (jint)glyphIndex);
}

int* GlyphVector::GetGlyphCharIndices(int* size, int beginGlyphIndex, int numEntries, int* codeReturn, int codeReturnc) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 4), (jint)beginGlyphIndex, (jint)numEntries, ToJIntArray(codeReturn, codeReturnc)));
}

int GlyphVector::GetGlyphCode(int glyphIndex) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsGlyphVector, 5), (jint)glyphIndex);
}

int* GlyphVector::GetGlyphCodes(int* size, int beginGlyphIndex, int numEntries, int* codeReturn, int codeReturnc) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 6), (jint)beginGlyphIndex, (jint)numEntries, ToJIntArray(codeReturn, codeReturnc)));
}

GlyphJustificationInfo GlyphVector::GetGlyphJustificationInfo(int glyphIndex) {
   return (GlyphJustificationInfo)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 7), (jint)glyphIndex));
}

Shape GlyphVector::GetGlyphLogicalBounds(int glyphIndex) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 8), (jint)glyphIndex));
}

GlyphMetrics GlyphVector::GetGlyphMetrics(int glyphIndex) {
   return (GlyphMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 9), (jint)glyphIndex));
}

Shape GlyphVector::GetGlyphOutline(int glyphIndex) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 10), (jint)glyphIndex));
}

Shape GlyphVector::GetGlyphOutline(int glyphIndex, float x, float y) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 11), (jint)glyphIndex, (jfloat)x, (jfloat)y));
}

Rectangle GlyphVector::GetGlyphPixelBounds(int index, FontRenderContext renderFRC, float x, float y) {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 12), (jint)index, renderFRC.instance, (jfloat)x, (jfloat)y));
}

geom::Point2D GlyphVector::GetGlyphPosition(int glyphIndex) {
   return (geom::Point2D)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 13), (jint)glyphIndex));
}

float* GlyphVector::GetGlyphPositions(int* size, int beginGlyphIndex, int numEntries, float* positionReturn, int positionReturnc) {
   return FromJFloatArray(size, (jfloatArray)tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 14), (jint)beginGlyphIndex, (jint)numEntries, ToJFloatArray(positionReturn, positionReturnc)));
}

geom::AffineTransform GlyphVector::GetGlyphTransform(int glyphIndex) {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 15), (jint)glyphIndex));
}

Shape GlyphVector::GetGlyphVisualBounds(int glyphIndex) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 16), (jint)glyphIndex));
}

int GlyphVector::GetLayoutFlags() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsGlyphVector, 17));
}

geom::Rectangle2D GlyphVector::GetLogicalBounds() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 18)));
}

int GlyphVector::GetNumGlyphs() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsGlyphVector, 19));
}

Shape GlyphVector::GetOutline() {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 20)));
}

Shape GlyphVector::GetOutline(float x, float y) {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 21), (jfloat)x, (jfloat)y));
}

Rectangle GlyphVector::GetPixelBounds(FontRenderContext renderFRC, float x, float y) {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 22), renderFRC.instance, (jfloat)x, (jfloat)y));
}

geom::Rectangle2D GlyphVector::GetVisualBounds() {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsGlyphVector, 23)));
}

void GlyphVector::PerformDefaultLayout() {
   tc::jEnv->CallVoidMethod(METHOD(clsGlyphVector, 24));
}

void GlyphVector::SetGlyphPosition(int glyphIndex, geom::Point2D newPos) {
   tc::jEnv->CallVoidMethod(METHOD(clsGlyphVector, 25), (jint)glyphIndex, newPos.instance);
}

void GlyphVector::SetGlyphTransform(int glyphIndex, geom::AffineTransform newTX) {
   tc::jEnv->CallVoidMethod(METHOD(clsGlyphVector, 26), (jint)glyphIndex, newTX.instance);
}


// java/awt/Font
// Name: Font
// Base Class(es): [lang::Object]

NativeObject clsFont;
jclass InitFont() {
   return tcInitNativeObject(clsFont, "java/awt/Font", { 
          { "canDisplay", "(C)Z", false }, 
          { "canDisplay", "(I)Z", false }, 
          { "canDisplayUpTo", "([CII)I", false }, 
          { "canDisplayUpTo", "(Ljava/text/CharacterIterator;II)I", false }, 
          { "canDisplayUpTo", "(Ljava/lang/String;)I", false }, 
          { "createFont", "(ILjava/io/File;)Ljava/awt/Font;", true }, 
          { "createFont", "(ILjava/io/InputStream;)Ljava/awt/Font;", true }, 
          { "createGlyphVector", "(Ljava/awt/font/FontRenderContext;[C)Ljava/awt/font/GlyphVector;", false }, 
          { "createGlyphVector", "(Ljava/awt/font/FontRenderContext;Ljava/text/CharacterIterator;)Ljava/awt/font/GlyphVector;", false }, 
          { "createGlyphVector", "(Ljava/awt/font/FontRenderContext;[I)Ljava/awt/font/GlyphVector;", false }, 
          { "createGlyphVector", "(Ljava/awt/font/FontRenderContext;Ljava/lang/String;)Ljava/awt/font/GlyphVector;", false }, 
          { "decode", "(Ljava/lang/String;)Ljava/awt/Font;", true }, 
          { "deriveFont", "(Ljava/awt/geom/AffineTransform;)Ljava/awt/Font;", false }, 
          { "deriveFont", "(F)Ljava/awt/Font;", false }, 
          { "deriveFont", "(I)Ljava/awt/Font;", false }, 
          { "deriveFont", "(ILjava/awt/geom/AffineTransform;)Ljava/awt/Font;", false }, 
          { "deriveFont", "(IF)Ljava/awt/Font;", false }, 
          { "deriveFont", "(Ljava/util/Map;)Ljava/awt/Font;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getAttributes", "()Ljava/util/Map;", false }, 
          { "getAvailableAttributes", "()[Ljava/text/AttributedCharacterIterator$Attribute;", false }, 
          { "getBaselineFor", "(C)B", false }, 
          { "getFamily", "()Ljava/lang/String;", false }, 
          { "getFont", "(Ljava/util/Map;)Ljava/awt/Font;", true }, 
          { "getFont", "(Ljava/lang/String;)Ljava/awt/Font;", true }, 
          { "getFont", "(Ljava/lang/String;Ljava/awt/Font;)Ljava/awt/Font;", true }, 
          { "getFontName", "()Ljava/lang/String;", false }, 
          { "getItalicAngle", "()F", false }, 
          { "getLineMetrics", "([CIILjava/awt/font/FontRenderContext;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/text/CharacterIterator;IILjava/awt/font/FontRenderContext;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/lang/String;Ljava/awt/font/FontRenderContext;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/lang/String;IILjava/awt/font/FontRenderContext;)Ljava/awt/font/LineMetrics;", false }, 
          { "getMaxCharBounds", "(Ljava/awt/font/FontRenderContext;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getMissingGlyphCode", "()I", false }, 
          { "getName", "()Ljava/lang/String;", false }, 
          { "getNumGlyphs", "()I", false }, 
          { "getPSName", "()Ljava/lang/String;", false }, 
          { "getSize", "()I", false }, 
          { "getSize2D", "()F", false }, 
          { "getStringBounds", "([CIILjava/awt/font/FontRenderContext;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/text/CharacterIterator;IILjava/awt/font/FontRenderContext;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/lang/String;Ljava/awt/font/FontRenderContext;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/lang/String;IILjava/awt/font/FontRenderContext;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStyle", "()I", false }, 
          { "getTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "hashCode", "()I", false }, 
          { "hasLayoutAttributes", "()Z", false }, 
          { "hasUniformLineMetrics", "()Z", false }, 
          { "isBold", "()Z", false }, 
          { "isItalic", "()Z", false }, 
          { "isPlain", "()Z", false }, 
          { "isTransformed", "()Z", false }, 
          { "layoutGlyphVector", "(Ljava/awt/font/FontRenderContext;[CIII)Ljava/awt/font/GlyphVector;", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Font::Font(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitFont();
}

Font::~Font() {}

Font::Font(util::Map attributes)
 : instance(tc::jEnv->NewObject(InitFont(), tc::jEnv->GetMethodID(InitFont(), "<init>", "(Ljava/util/Map;)V"), attributes.instance)), lang::Object(instance)
{}

Font::Font(const char* name, int style, int size)
 : instance(tc::jEnv->NewObject(InitFont(), tc::jEnv->GetMethodID(InitFont(), "<init>", "(Ljava/lang/String;II)V"), ToJString(name), (jint)style, (jint)size)), lang::Object(instance)
{}

bool Font::CanDisplay(char c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 0), (jchar)c);
}

bool Font::CanDisplay(int codePoint) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 1), (jint)codePoint);
}

int Font::CanDisplayUpTo(const char* text, int textc, int start, int limit) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 2), ToJString(text), (jint)start, (jint)limit);
}

int Font::CanDisplayUpTo(text::CharacterIterator iter, int start, int limit) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 3), iter.instance, (jint)start, (jint)limit);
}

int Font::CanDisplayUpTo(const char* str) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 4), ToJString(str));
}

Font Font::CreateFont(int fontFormat, io::File fontFile) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[5], (jint)fontFormat, fontFile.instance));
}

Font Font::CreateFont(int fontFormat, io::InputStream fontStream) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[6], (jint)fontFormat, fontStream.instance));
}

font::GlyphVector Font::CreateGlyphVector(font::FontRenderContext frc, const char* chars, int charsc) {
   return (font::GlyphVector)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 7), frc.instance, ToJString(chars)));
}

font::GlyphVector Font::CreateGlyphVector(font::FontRenderContext frc, text::CharacterIterator ci) {
   return (font::GlyphVector)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 8), frc.instance, ci.instance));
}

font::GlyphVector Font::CreateGlyphVector(font::FontRenderContext frc, int* glyphCodes, int glyphCodesc) {
   return (font::GlyphVector)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 9), frc.instance, ToJIntArray(glyphCodes, glyphCodesc)));
}

font::GlyphVector Font::CreateGlyphVector(font::FontRenderContext frc, const char* str) {
   return (font::GlyphVector)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 10), frc.instance, ToJString(str)));
}

Font Font::Decode(const char* str) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[11], ToJString(str)));
}

Font Font::DeriveFont(geom::AffineTransform trans) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 12), trans.instance));
}

Font Font::DeriveFont(float size) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 13), (jfloat)size));
}

Font Font::DeriveFont(int style) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 14), (jint)style));
}

Font Font::DeriveFont(int style, geom::AffineTransform trans) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 15), (jint)style, trans.instance));
}

Font Font::DeriveFont(int style, float size) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 16), (jint)style, (jfloat)size));
}

Font Font::DeriveFont(util::Map attributes) {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 17), attributes.instance));
}

bool Font::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 18), obj.instance);
}

util::Map Font::GetAttributes() {
   return (util::Map)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 19)));
}

text::AttributedCharacterIterator::Attribute* Font::GetAvailableAttributes(int* size) {
   return FromJObjectArray<text::AttributedCharacterIterator::Attribute>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFont, 20)));
}

signed char Font::GetBaselineFor(char c) {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsFont, 21), (jchar)c);
}

char* Font::GetFamily() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFont, 22)));
}

Font Font::GetFont(util::Map map) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[23], map.instance));
}

Font Font::GetFont(const char* nm) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[24], ToJString(nm)));
}

Font Font::GetFont(const char* nm, Font font) {
   InitFont();
   return (Font)(tc::jEnv->CallStaticObjectMethod(clsFont.clazz, clsFont.methods[25], ToJString(nm), font.instance));
}

char* Font::GetFontName() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFont, 26)));
}

float Font::GetItalicAngle() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsFont, 27));
}

font::LineMetrics Font::GetLineMetrics(const char* chars, int charsc, int beginIndex, int limit, font::FontRenderContext frc) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 28), ToJString(chars), (jint)beginIndex, (jint)limit, frc.instance));
}

font::LineMetrics Font::GetLineMetrics(text::CharacterIterator ci, int beginIndex, int limit, font::FontRenderContext frc) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 29), ci.instance, (jint)beginIndex, (jint)limit, frc.instance));
}

font::LineMetrics Font::GetLineMetrics(const char* str, font::FontRenderContext frc) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 30), ToJString(str), frc.instance));
}

font::LineMetrics Font::GetLineMetrics(const char* str, int beginIndex, int limit, font::FontRenderContext frc) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 31), ToJString(str), (jint)beginIndex, (jint)limit, frc.instance));
}

geom::Rectangle2D Font::GetMaxCharBounds(font::FontRenderContext frc) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 32), frc.instance));
}

int Font::GetMissingGlyphCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 33));
}

char* Font::GetName() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFont, 34)));
}

int Font::GetNumGlyphs() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 35));
}

char* Font::GetPSName() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFont, 36)));
}

int Font::GetSize() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 37));
}

float Font::GetSize2D() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsFont, 38));
}

geom::Rectangle2D Font::GetStringBounds(const char* chars, int charsc, int beginIndex, int limit, font::FontRenderContext frc) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 39), ToJString(chars), (jint)beginIndex, (jint)limit, frc.instance));
}

geom::Rectangle2D Font::GetStringBounds(text::CharacterIterator ci, int beginIndex, int limit, font::FontRenderContext frc) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 40), ci.instance, (jint)beginIndex, (jint)limit, frc.instance));
}

geom::Rectangle2D Font::GetStringBounds(const char* str, font::FontRenderContext frc) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 41), ToJString(str), frc.instance));
}

geom::Rectangle2D Font::GetStringBounds(const char* str, int beginIndex, int limit, font::FontRenderContext frc) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 42), ToJString(str), (jint)beginIndex, (jint)limit, frc.instance));
}

int Font::GetStyle() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 43));
}

geom::AffineTransform Font::GetTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 44)));
}

int Font::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFont, 45));
}

bool Font::HasLayoutAttributes() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 46));
}

bool Font::HasUniformLineMetrics() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 47));
}

bool Font::IsBold() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 48));
}

bool Font::IsItalic() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 49));
}

bool Font::IsPlain() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 50));
}

bool Font::IsTransformed() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFont, 51));
}

font::GlyphVector Font::LayoutGlyphVector(font::FontRenderContext frc, const char* text, int textc, int start, int limit, int flags) {
   return (font::GlyphVector)(tc::jEnv->CallObjectMethod(METHOD(clsFont, 52), frc.instance, ToJString(text), (jint)start, (jint)limit, (jint)flags));
}

char* Font::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFont, 53)));
}


// java/awt/FontMetrics
// Name: FontMetrics
// Base Class(es): [lang::Object]

NativeObject clsFontMetrics;
jclass InitFontMetrics() {
   return tcInitNativeObject(clsFontMetrics, "java/awt/FontMetrics", { 
          { "signed charsWidth", "([BII)I", false }, 
          { "charsWidth", "([CII)I", false }, 
          { "charWidth", "(C)I", false }, 
          { "charWidth", "(I)I", false }, 
          { "getAscent", "()I", false }, 
          { "getDescent", "()I", false }, 
          { "getFont", "()Ljava/awt/Font;", false }, 
          { "getFontRenderContext", "()Ljava/awt/font/FontRenderContext;", false }, 
          { "getHeight", "()I", false }, 
          { "getLeading", "()I", false }, 
          { "getLineMetrics", "([CIILjava/awt/Graphics;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/text/CharacterIterator;IILjava/awt/Graphics;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/lang/String;Ljava/awt/Graphics;)Ljava/awt/font/LineMetrics;", false }, 
          { "getLineMetrics", "(Ljava/lang/String;IILjava/awt/Graphics;)Ljava/awt/font/LineMetrics;", false }, 
          { "getMaxAdvance", "()I", false }, 
          { "getMaxAscent", "()I", false }, 
          { "getMaxCharBounds", "(Ljava/awt/Graphics;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getMaxDescent", "()I", false }, 
          { "getStringBounds", "([CIILjava/awt/Graphics;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/text/CharacterIterator;IILjava/awt/Graphics;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/lang/String;Ljava/awt/Graphics;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getStringBounds", "(Ljava/lang/String;IILjava/awt/Graphics;)Ljava/awt/geom/Rectangle2D;", false }, 
          { "getWidths", "()[I", false }, 
          { "hasUniformLineMetrics", "()Z", false }, 
          { "stringWidth", "(Ljava/lang/String;)I", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

FontMetrics::FontMetrics(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitFontMetrics();
}

FontMetrics::~FontMetrics() {}

int FontMetrics::BytesWidth(signed char* data, int datac, int off, int len) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 0), ToJByteArray(data, datac), (jint)off, (jint)len);
}

int FontMetrics::CharsWidth(char* data, int datac, int off, int len) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 1), ToJString(data), (jint)off, (jint)len);
}

int FontMetrics::CharWidth(char ch) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 2), (jchar)ch);
}

int FontMetrics::CharWidth(int codePoint) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 3), (jint)codePoint);
}

int FontMetrics::GetAscent() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 4));
}

int FontMetrics::GetDescent() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 5));
}

Font FontMetrics::GetFont() {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 6)));
}

font::FontRenderContext FontMetrics::GetFontRenderContext() {
   return (font::FontRenderContext)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 7)));
}

int FontMetrics::GetHeight() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 8));
}

int FontMetrics::GetLeading() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 9));
}

font::LineMetrics FontMetrics::GetLineMetrics(const char* chars, int charsc, int beginIndex, int limit, Graphics context) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 10), ToJString(chars), (jint)beginIndex, (jint)limit, context.instance));
}

font::LineMetrics FontMetrics::GetLineMetrics(text::CharacterIterator ci, int beginIndex, int limit, Graphics context) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 11), ci.instance, (jint)beginIndex, (jint)limit, context.instance));
}

font::LineMetrics FontMetrics::GetLineMetrics(const char* str, Graphics context) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 12), ToJString(str), context.instance));
}

font::LineMetrics FontMetrics::GetLineMetrics(const char* str, int beginIndex, int limit, Graphics context) {
   return (font::LineMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 13), ToJString(str), (jint)beginIndex, (jint)limit, context.instance));
}

int FontMetrics::GetMaxAdvance() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 14));
}

int FontMetrics::GetMaxAscent() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 15));
}

geom::Rectangle2D FontMetrics::GetMaxCharBounds(Graphics context) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 16), context.instance));
}

int FontMetrics::GetMaxDescent() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 17));
}

geom::Rectangle2D FontMetrics::GetStringBounds(const char* chars, int charsc, int beginIndex, int limit, Graphics context) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 18), ToJString(chars), (jint)beginIndex, (jint)limit, context.instance));
}

geom::Rectangle2D FontMetrics::GetStringBounds(text::CharacterIterator ci, int beginIndex, int limit, Graphics context) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 19), ci.instance, (jint)beginIndex, (jint)limit, context.instance));
}

geom::Rectangle2D FontMetrics::GetStringBounds(const char* str, Graphics context) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 20), ToJString(str), context.instance));
}

geom::Rectangle2D FontMetrics::GetStringBounds(const char* str, int beginIndex, int limit, Graphics context) {
   return (geom::Rectangle2D)(tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 21), ToJString(str), (jint)beginIndex, (jint)limit, context.instance));
}

int* FontMetrics::GetWidths(int* size) {
   return FromJIntArray(size, (jintArray)tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 22)));
}

bool FontMetrics::HasUniformLineMetrics() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFontMetrics, 23));
}

int FontMetrics::StringWidth(const char* str) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFontMetrics, 24), ToJString(str));
}

char* FontMetrics::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFontMetrics, 25)));
}


// java/awt/Graphics
// Name: Graphics
// Base Class(es): [lang::Object]

NativeObject clsGraphics;
jclass InitGraphics() {
   return tcInitNativeObject(clsGraphics, "java/awt/Graphics", { 
          { "clearRect", "(IIII)V", false }, 
          { "clipRect", "(IIII)V", false }, 
          { "copyArea", "(IIIIII)V", false }, 
          { "create", "()Ljava/awt/Graphics;", false }, 
          { "create", "(IIII)Ljava/awt/Graphics;", false }, 
          { "dispose", "()V", false }, 
          { "draw3DRect", "(IIIIZ)V", false }, 
          { "drawArc", "(IIIIII)V", false }, 
          { "drawBytes", "([BIIII)V", false }, 
          { "drawChars", "([CIIII)V", false }, 
          { "drawImage", "(Ljava/awt/Image;IILjava/awt/Color;Ljava/awt/image/ImageObserver;)Z", false }, 
          { "drawImage", "(Ljava/awt/Image;IILjava/awt/image/ImageObserver;)Z", false }, 
          { "drawImage", "(Ljava/awt/Image;IIIILjava/awt/Color;Ljava/awt/image/ImageObserver;)Z", false }, 
          { "drawImage", "(Ljava/awt/Image;IIIILjava/awt/image/ImageObserver;)Z", false }, 
          { "drawImage", "(Ljava/awt/Image;IIIIIIIILjava/awt/Color;Ljava/awt/image/ImageObserver;)Z", false }, 
          { "drawImage", "(Ljava/awt/Image;IIIIIIIILjava/awt/image/ImageObserver;)Z", false }, 
          { "drawLine", "(IIII)V", false }, 
          { "drawOval", "(IIII)V", false }, 
          { "drawPolygon", "([I[II)V", false }, 
          { "drawPolygon", "(Ljava/awt/Polygon;)V", false }, 
          { "drawPolyline", "([I[II)V", false }, 
          { "drawRoundRect", "(IIIIII)V", false }, 
          { "drawString", "(Ljava/text/AttributedCharacterIterator;II)V", false }, 
          { "drawString", "(Ljava/lang/String;II)V", false }, 
          { "fill3DRect", "(IIIIZ)V", false }, 
          { "fillArc", "(IIIIII)V", false }, 
          { "fillOval", "(IIII)V", false }, 
          { "fillPolygon", "([I[II)V", false }, 
          { "fillPolygon", "(Ljava/awt/Polygon;)V", false }, 
          { "fillRect", "(IIII)V", false }, 
          { "fillRoundRect", "(IIIIII)V", false }, 
          { "finalize", "()V", false }, 
          { "getClip", "()Ljava/awt/Shape;", false }, 
          { "getClipBounds", "()Ljava/awt/Rectangle;", false }, 
          { "getClipBounds", "(Ljava/awt/Rectangle;)Ljava/awt/Rectangle;", false }, 
          { "getColor", "()Ljava/awt/Color;", false }, 
          { "getFont", "()Ljava/awt/Font;", false }, 
          { "getFontMetrics", "()Ljava/awt/FontMetrics;", false }, 
          { "getFontMetrics", "(Ljava/awt/Font;)Ljava/awt/FontMetrics;", false }, 
          { "hitClip", "(IIII)Z", false }, 
          { "setClip", "(IIII)V", false }, 
          { "setClip", "(Ljava/awt/Shape;)V", false }, 
          { "setColor", "(Ljava/awt/Color;)V", false }, 
          { "setFont", "(Ljava/awt/Font;)V", false }, 
          { "setPaintMode", "()V", false }, 
          { "setXORMode", "(Ljava/awt/Color;)V", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "translate", "(II)V", false }
       }, { 
       });
}

Graphics::Graphics(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitGraphics();
}

Graphics::~Graphics() {}

void Graphics::ClearRect(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 0), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::ClipRect(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 1), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::CopyArea(int x, int y, int width, int height, int dx, int dy) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 2), (jint)x, (jint)y, (jint)width, (jint)height, (jint)dx, (jint)dy);
}

Graphics Graphics::Create() {
   return (Graphics)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 3)));
}

Graphics Graphics::Create(int x, int y, int width, int height) {
   return (Graphics)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 4), (jint)x, (jint)y, (jint)width, (jint)height));
}

void Graphics::Dispose() {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 5));
}

void Graphics::Draw3DRect(int x, int y, int width, int height, bool raised) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 6), (jint)x, (jint)y, (jint)width, (jint)height, (jboolean)raised);
}

void Graphics::DrawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 7), (jint)x, (jint)y, (jint)width, (jint)height, (jint)startAngle, (jint)arcAngle);
}

void Graphics::DrawBytes(signed char* data, int datac, int offset, int length, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 8), ToJByteArray(data, datac), (jint)offset, (jint)length, (jint)x, (jint)y);
}

void Graphics::DrawChars(char* data, int datac, int offset, int length, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 9), ToJString(data), (jint)offset, (jint)length, (jint)x, (jint)y);
}

bool Graphics::DrawImage(Image img, int x, int y, Color bgcolor, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 10), img.instance, (jint)x, (jint)y, bgcolor.instance, observer.instance);
}

bool Graphics::DrawImage(Image img, int x, int y, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 11), img.instance, (jint)x, (jint)y, observer.instance);
}

bool Graphics::DrawImage(Image img, int x, int y, int width, int height, Color bgcolor, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 12), img.instance, (jint)x, (jint)y, (jint)width, (jint)height, bgcolor.instance, observer.instance);
}

bool Graphics::DrawImage(Image img, int x, int y, int width, int height, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 13), img.instance, (jint)x, (jint)y, (jint)width, (jint)height, observer.instance);
}

bool Graphics::DrawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 14), img.instance, (jint)dx1, (jint)dy1, (jint)dx2, (jint)dy2, (jint)sx1, (jint)sy1, (jint)sx2, (jint)sy2, bgcolor.instance, observer.instance);
}

bool Graphics::DrawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, image::ImageObserver observer) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 15), img.instance, (jint)dx1, (jint)dy1, (jint)dx2, (jint)dy2, (jint)sx1, (jint)sy1, (jint)sx2, (jint)sy2, observer.instance);
}

void Graphics::DrawLine(int x1, int y1, int x2, int y2) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 16), (jint)x1, (jint)y1, (jint)x2, (jint)y2);
}

void Graphics::DrawOval(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 17), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::DrawPolygon(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 18), ToJIntArray(xPoints, xPointsc), ToJIntArray(yPoints, yPointsc), (jint)nPoints);
}

void Graphics::DrawPolygon(Polygon p) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 19), p.instance);
}

void Graphics::DrawPolyline(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 20), ToJIntArray(xPoints, xPointsc), ToJIntArray(yPoints, yPointsc), (jint)nPoints);
}

void Graphics::DrawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 21), (jint)x, (jint)y, (jint)width, (jint)height, (jint)arcWidth, (jint)arcHeight);
}

void Graphics::DrawString(text::AttributedCharacterIterator iterator, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 22), iterator.instance, (jint)x, (jint)y);
}

void Graphics::DrawString(const char* str, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 23), ToJString(str), (jint)x, (jint)y);
}

void Graphics::Fill3DRect(int x, int y, int width, int height, bool raised) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 24), (jint)x, (jint)y, (jint)width, (jint)height, (jboolean)raised);
}

void Graphics::FillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 25), (jint)x, (jint)y, (jint)width, (jint)height, (jint)startAngle, (jint)arcAngle);
}

void Graphics::FillOval(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 26), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::FillPolygon(int* xPoints, int xPointsc, int* yPoints, int yPointsc, int nPoints) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 27), ToJIntArray(xPoints, xPointsc), ToJIntArray(yPoints, yPointsc), (jint)nPoints);
}

void Graphics::FillPolygon(Polygon p) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 28), p.instance);
}

void Graphics::FillRect(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 29), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::FillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 30), (jint)x, (jint)y, (jint)width, (jint)height, (jint)arcWidth, (jint)arcHeight);
}

void Graphics::Finalize() {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 31));
}

Shape Graphics::GetClip() {
   return (Shape)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 32)));
}

Rectangle Graphics::GetClipBounds() {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 33)));
}

Rectangle Graphics::GetClipBounds(Rectangle r) {
   return (Rectangle)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 34), r.instance));
}

Color Graphics::GetColor() {
   return (Color)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 35)));
}

Font Graphics::GetFont() {
   return (Font)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 36)));
}

FontMetrics Graphics::GetFontMetrics() {
   return (FontMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 37)));
}

FontMetrics Graphics::GetFontMetrics(Font f) {
   return (FontMetrics)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 38), f.instance));
}

bool Graphics::HitClip(int x, int y, int width, int height) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics, 39), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::SetClip(int x, int y, int width, int height) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 40), (jint)x, (jint)y, (jint)width, (jint)height);
}

void Graphics::SetClip(Shape clip) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 41), clip.instance);
}

void Graphics::SetColor(Color c) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 42), c.instance);
}

void Graphics::SetFont(Font font) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 43), font.instance);
}

void Graphics::SetPaintMode() {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 44));
}

void Graphics::SetXORMode(Color c1) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 45), c1.instance);
}

char* Graphics::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsGraphics, 46)));
}

void Graphics::Translate(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics, 47), (jint)x, (jint)y);
}


// java/awt/Graphics2D
// Name: Graphics2D
// Base Class(es): [Graphics]

NativeObject clsGraphics2D;
jclass InitGraphics2D() {
   return tcInitNativeObject(clsGraphics2D, "java/awt/Graphics2D", { 
          { "addRenderingHints", "(Ljava/util/Map;)V", false }, 
          { "clip", "(Ljava/awt/Shape;)V", false }, 
          { "draw", "(Ljava/awt/Shape;)V", false }, 
          { "draw3DRect", "(IIIIZ)V", false }, 
          { "drawGlyphVector", "(Ljava/awt/font/GlyphVector;FF)V", false }, 
          { "drawImage", "(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImageOp;II)V", false }, 
          { "drawImage", "(Ljava/awt/Image;Ljava/awt/geom/AffineTransform;Ljava/awt/image/ImageObserver;)Z", false }, 
          { "drawRenderableImage", "(Ljava/awt/image/renderable/RenderableImage;Ljava/awt/geom/AffineTransform;)V", false }, 
          { "drawRenderedImage", "(Ljava/awt/image/RenderedImage;Ljava/awt/geom/AffineTransform;)V", false }, 
          { "drawString", "(Ljava/text/AttributedCharacterIterator;FF)V", false }, 
          { "drawString", "(Ljava/text/AttributedCharacterIterator;II)V", false }, 
          { "fill", "(Ljava/awt/Shape;)V", false }, 
          { "fill3DRect", "(IIIIZ)V", false }, 
          { "getBackground", "()Ljava/awt/Color;", false }, 
          { "getComposite", "()Ljava/awt/Composite;", false }, 
          { "getDeviceConfiguration", "()Ljava/awt/GraphicsConfiguration;", false }, 
          { "getFontRenderContext", "()Ljava/awt/font/FontRenderContext;", false }, 
          { "getPaint", "()Ljava/awt/Paint;", false }, 
          { "getRenderingHint", "(Ljava/awt/RenderingHints$Key;)Ljava/lang/Object;", false }, 
          { "getRenderingHints", "()Ljava/awt/RenderingHints;", false }, 
          { "getStroke", "()Ljava/awt/Stroke;", false }, 
          { "getTransform", "()Ljava/awt/geom/AffineTransform;", false }, 
          { "hit", "(Ljava/awt/Rectangle;Ljava/awt/Shape;Z)Z", false }, 
          { "rotate", "(D)V", false }, 
          { "rotate", "(DDD)V", false }, 
          { "scale", "(DD)V", false }, 
          { "setBackground", "(Ljava/awt/Color;)V", false }, 
          { "setComposite", "(Ljava/awt/Composite;)V", false }, 
          { "setPaint", "(Ljava/awt/Paint;)V", false }, 
          { "setRenderingHint", "(Ljava/awt/RenderingHints$Key;Ljava/lang/Object;)V", false }, 
          { "setRenderingHints", "(Ljava/util/Map;)V", false }, 
          { "setStroke", "(Ljava/awt/Stroke;)V", false }, 
          { "setTransform", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "shear", "(DD)V", false }, 
          { "transform", "(Ljava/awt/geom/AffineTransform;)V", false }, 
          { "translate", "(DD)V", false }, 
          { "translate", "(II)V", false }
       }, { 
       });
}

Graphics2D::Graphics2D(jobject instance)
 : instance(instance), Graphics(instance) {
    InitGraphics2D();
}

Graphics2D::~Graphics2D() {}

void Graphics2D::AddRenderingHints(util::Map hints) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 0), hints.instance);
}

void Graphics2D::Clip(Shape s) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 1), s.instance);
}

void Graphics2D::Draw(Shape s) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 2), s.instance);
}

void Graphics2D::Draw3DRect(int x, int y, int width, int height, bool raised) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 3), (jint)x, (jint)y, (jint)width, (jint)height, (jboolean)raised);
}

void Graphics2D::DrawGlyphVector(font::GlyphVector g, float x, float y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 4), g.instance, (jfloat)x, (jfloat)y);
}

void Graphics2D::DrawImage(image::BufferedImage img, image::BufferedImageOp op, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 5), img.instance, op.instance, (jint)x, (jint)y);
}

bool Graphics2D::DrawImage(Image img, geom::AffineTransform xform, image::ImageObserver obs) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics2D, 6), img.instance, xform.instance, obs.instance);
}

void Graphics2D::DrawRenderableImage(image::renderable::RenderableImage img, geom::AffineTransform xform) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 7), img.instance, xform.instance);
}

void Graphics2D::DrawRenderedImage(image::RenderedImage img, geom::AffineTransform xform) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 8), img.instance, xform.instance);
}

void Graphics2D::DrawString(text::AttributedCharacterIterator iterator, float x, float y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 9), iterator.instance, (jfloat)x, (jfloat)y);
}

void Graphics2D::DrawString(text::AttributedCharacterIterator iterator, int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 10), iterator.instance, (jint)x, (jint)y);
}

void Graphics2D::Fill(Shape s) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 11), s.instance);
}

void Graphics2D::Fill3DRect(int x, int y, int width, int height, bool raised) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 12), (jint)x, (jint)y, (jint)width, (jint)height, (jboolean)raised);
}

Color Graphics2D::GetBackground() {
   return (Color)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 13)));
}

Composite Graphics2D::GetComposite() {
   return (Composite)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 14)));
}

GraphicsConfiguration Graphics2D::GetDeviceConfiguration() {
   return (GraphicsConfiguration)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 15)));
}

font::FontRenderContext Graphics2D::GetFontRenderContext() {
   return (font::FontRenderContext)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 16)));
}

Paint Graphics2D::GetPaint() {
   return (Paint)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 17)));
}

lang::Object Graphics2D::GetRenderingHint(RenderingHints::Key hintKey) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 18), hintKey.instance));
}

RenderingHints Graphics2D::GetRenderingHints() {
   return (RenderingHints)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 19)));
}

Stroke Graphics2D::GetStroke() {
   return (Stroke)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 20)));
}

geom::AffineTransform Graphics2D::GetTransform() {
   return (geom::AffineTransform)(tc::jEnv->CallObjectMethod(METHOD(clsGraphics2D, 21)));
}

bool Graphics2D::Hit(Rectangle rect, Shape shape, bool onStroke) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsGraphics2D, 22), rect.instance, shape.instance, (jboolean)onStroke);
}

void Graphics2D::Rotate(double theta) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 23), (jdouble)theta);
}

void Graphics2D::Rotate(double theta, double x, double y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 24), (jdouble)theta, (jdouble)x, (jdouble)y);
}

void Graphics2D::Scale(double sx, double sy) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 25), (jdouble)sx, (jdouble)sy);
}

void Graphics2D::SetBackground(Color color) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 26), color.instance);
}

void Graphics2D::SetComposite(Composite comp) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 27), comp.instance);
}

void Graphics2D::SetPaint(Paint paint) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 28), paint.instance);
}

void Graphics2D::SetRenderingHint(RenderingHints::Key hintKey, lang::Object hintValue) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 29), hintKey.instance, hintValue.instance);
}

void Graphics2D::SetRenderingHints(util::Map hints) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 30), hints.instance);
}

void Graphics2D::SetStroke(Stroke s) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 31), s.instance);
}

void Graphics2D::SetTransform(geom::AffineTransform Tx) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 32), Tx.instance);
}

void Graphics2D::Shear(double shx, double shy) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 33), (jdouble)shx, (jdouble)shy);
}

void Graphics2D::Transform(geom::AffineTransform Tx) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 34), Tx.instance);
}

void Graphics2D::Translate(double tx, double ty) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 35), (jdouble)tx, (jdouble)ty);
}

void Graphics2D::Translate(int x, int y) {
   tc::jEnv->CallVoidMethod(METHOD(clsGraphics2D, 36), (jint)x, (jint)y);
}


