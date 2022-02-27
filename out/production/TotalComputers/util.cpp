#include "j_env.h"
#include "utils.h"
#include "util.h"

using namespace util;

// java/util/Enumeration
// Name: Enumeration
// Base Class(es): [NativeObjectInstance]

NativeObject clsEnumeration;
jclass InitEnumeration() {
   return tcInitNativeObject(clsEnumeration, "java/util/Enumeration", { 
          { "hasMoreElements", "()Z", false }, 
          { "nextElement", "()Ljava/lang/Object;", false }
       }, { 
       });
}

Enumeration::Enumeration(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitEnumeration();
}

Enumeration::~Enumeration() {}

bool Enumeration::HasMoreElements() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsEnumeration, 0));
}

lang::Object Enumeration::NextElement() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsEnumeration, 1)));
}


// java/util/Iterator
// Name: Iterator
// Base Class(es): [NativeObjectInstance]

NativeObject clsIterator;
jclass InitIterator() {
   return tcInitNativeObject(clsIterator, "java/util/Iterator", { 
          { "hasNext", "()Z", false }, 
          { "next", "()Ljava/lang/Object;", false }, 
          { "remove", "()V", false }
       }, { 
       });
}

Iterator::Iterator(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitIterator();
}

Iterator::~Iterator() {}

bool Iterator::HasNext() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsIterator, 0));
}

lang::Object Iterator::Next() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsIterator, 1)));
}

void Iterator::Remove() {
   tc::jEnv->CallVoidMethod(METHOD(clsIterator, 2));
}


// java/util/ListIterator
// Name: ListIterator
// Base Class(es): [Iterator]

NativeObject clsListIterator;
jclass InitListIterator() {
   return tcInitNativeObject(clsListIterator, "java/util/ListIterator", { 
          { "add", "(Ljava/lang/Object;)V", false }, 
          { "hasNext", "()Z", false }, 
          { "hasPrevious", "()Z", false }, 
          { "next", "()Ljava/lang/Object;", false }, 
          { "nextIndex", "()I", false }, 
          { "previous", "()Ljava/lang/Object;", false }, 
          { "previousIndex", "()I", false }, 
          { "remove", "()V", false }, 
          { "set", "(Ljava/lang/Object;)V", false }
       }, { 
       });
}

ListIterator::ListIterator(jobject instance)
 : instance(instance), Iterator(instance) {
    InitListIterator();
}

ListIterator::~ListIterator() {}

void ListIterator::Add(lang::Object e) {
   tc::jEnv->CallVoidMethod(METHOD(clsListIterator, 0), e.instance);
}

bool ListIterator::HasNext() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsListIterator, 1));
}

bool ListIterator::HasPrevious() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsListIterator, 2));
}

lang::Object ListIterator::Next() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsListIterator, 3)));
}

int ListIterator::NextIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsListIterator, 4));
}

lang::Object ListIterator::Previous() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsListIterator, 5)));
}

int ListIterator::PreviousIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsListIterator, 6));
}

void ListIterator::Remove() {
   tc::jEnv->CallVoidMethod(METHOD(clsListIterator, 7));
}

void ListIterator::Set(lang::Object e) {
   tc::jEnv->CallVoidMethod(METHOD(clsListIterator, 8), e.instance);
}


// java/util/Collection
// Name: Collection
// Base Class(es): [lang::Iterable]

NativeObject clsCollection;
jclass InitCollection() {
   return tcInitNativeObject(clsCollection, "java/util/Collection", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "addAll", "(Ljava/util/Collection;)Z", false }, 
          { "clear", "()V", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsAll", "(Ljava/util/Collection;)Z", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "isEmpty", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "remove", "(Ljava/lang/Object;)Z", false }, 
          { "removeAll", "(Ljava/util/Collection;)Z", false }, 
          { "retainAll", "(Ljava/util/Collection;)Z", false }, 
          { "size", "()I", false }, 
          { "toArray", "()[Ljava/lang/Object;", false }, 
          { "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", false }
       }, { 
       });
}

Collection::Collection(jobject instance)
 : instance(instance), lang::Iterable(instance) {
    InitCollection();
}

Collection::~Collection() {}

bool Collection::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 0), e.instance);
}

bool Collection::AddAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 1), c.instance);
}

void Collection::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsCollection, 2));
}

bool Collection::Contains(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 3), o.instance);
}

bool Collection::ContainsAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 4), c.instance);
}

bool Collection::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 5), o.instance);
}

int Collection::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsCollection, 6));
}

bool Collection::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 7));
}

Iterator Collection::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsCollection, 8)));
}

bool Collection::Remove(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 9), o.instance);
}

bool Collection::RemoveAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 10), c.instance);
}

bool Collection::RetainAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsCollection, 11), c.instance);
}

int Collection::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsCollection, 12));
}

lang::Object* Collection::ToArray(int* size) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsCollection, 13)));
}

lang::Object* Collection::ToArray(int* size, lang::Object* a, int ac) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsCollection, 14), ToJObjectArray<lang::Object>(a, ac)));
}


// java/util/Set
// Name: Set
// Base Class(es): [Collection]

NativeObject clsSet;
jclass InitSet() {
   return tcInitNativeObject(clsSet, "java/util/Set", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "addAll", "(Ljava/util/Collection;)Z", false }, 
          { "clear", "()V", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsAll", "(Ljava/util/Collection;)Z", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "isEmpty", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "remove", "(Ljava/lang/Object;)Z", false }, 
          { "removeAll", "(Ljava/util/Collection;)Z", false }, 
          { "retainAll", "(Ljava/util/Collection;)Z", false }, 
          { "size", "()I", false }, 
          { "toArray", "()[Ljava/lang/Object;", false }, 
          { "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", false }
       }, { 
       });
}

Set::Set(jobject instance)
 : instance(instance), Collection(instance) {
    InitSet();
}

Set::~Set() {}

bool Set::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 0), e.instance);
}

bool Set::AddAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 1), c.instance);
}

void Set::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsSet, 2));
}

bool Set::Contains(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 3), o.instance);
}

bool Set::ContainsAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 4), c.instance);
}

bool Set::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 5), o.instance);
}

int Set::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSet, 6));
}

bool Set::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 7));
}

Iterator Set::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsSet, 8)));
}

bool Set::Remove(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 9), o.instance);
}

bool Set::RemoveAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 10), c.instance);
}

bool Set::RetainAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsSet, 11), c.instance);
}

int Set::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsSet, 12));
}

lang::Object* Set::ToArray(int* size) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsSet, 13)));
}

lang::Object* Set::ToArray(int* size, lang::Object* a, int ac) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsSet, 14), ToJObjectArray<lang::Object>(a, ac)));
}


// java/util/Map
// Name: Map
// Base Class(es): [NativeObjectInstance]

NativeObject clsMap;
jclass InitMap() {
   return tcInitNativeObject(clsMap, "java/util/Map", { 
          { "clear", "()V", false }, 
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
          { "values", "()Ljava/util/Collection;", false }
       }, { 
       });
}

Map::Map(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitMap();
}

Map::~Map() {}

void Map::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsMap, 0));
}

bool Map::ContainsKey(lang::Object key) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsMap, 1), key.instance);
}

bool Map::ContainsValue(lang::Object value) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsMap, 2), value.instance);
}

Set Map::EntrySet() {
   return (Set)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 3)));
}

bool Map::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsMap, 4), o.instance);
}

lang::Object Map::Get(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 5), key.instance));
}

int Map::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsMap, 6));
}

bool Map::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsMap, 7));
}

Set Map::KeySet() {
   return (Set)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 8)));
}

lang::Object Map::Put(lang::Object key, lang::Object value) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 9), key.instance, value.instance));
}

void Map::PutAll(Map m) {
   tc::jEnv->CallVoidMethod(METHOD(clsMap, 10), m.instance);
}

lang::Object Map::Remove(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 11), key.instance));
}

int Map::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsMap, 12));
}

Collection Map::Values() {
   return (Collection)(tc::jEnv->CallObjectMethod(METHOD(clsMap, 13)));
}


// java/util/Map/Entry
// Name: Map::Entry
// Base Class(es): [NativeObjectInstance]

NativeObject clsMap_Entry;
jclass InitMap_Entry() {
   return tcInitNativeObject(clsMap_Entry, "java/util/Map/Entry", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getKey", "()Ljava/lang/Object;", false }, 
          { "getValue", "()Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "setValue", "(Ljava/lang/Object;)Ljava/lang/Object;", false }
       }, { 
       });
}

Map::Entry::Entry(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitMap_Entry();
}

Map::Entry::~Entry() {}

bool Map::Entry::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsMap_Entry, 0), o.instance);
}

lang::Object Map::Entry::GetKey() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap_Entry, 1)));
}

lang::Object Map::Entry::GetValue() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap_Entry, 2)));
}

int Map::Entry::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsMap_Entry, 3));
}

lang::Object Map::Entry::SetValue(lang::Object value) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsMap_Entry, 4), value.instance));
}


// java/util/AbstractCollection
// Name: AbstractCollection
// Base Class(es): [Collection, lang::Object]

NativeObject clsAbstractCollection;
jclass InitAbstractCollection() {
   return tcInitNativeObject(clsAbstractCollection, "java/util/AbstractCollection", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "addAll", "(Ljava/util/Collection;)Z", false }, 
          { "clear", "()V", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsAll", "(Ljava/util/Collection;)Z", false }, 
          { "isEmpty", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "remove", "(Ljava/lang/Object;)Z", false }, 
          { "removeAll", "(Ljava/util/Collection;)Z", false }, 
          { "retainAll", "(Ljava/util/Collection;)Z", false }, 
          { "size", "()I", false }, 
          { "toArray", "()[Ljava/lang/Object;", false }, 
          { "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

AbstractCollection::AbstractCollection(jobject instance)
 : instance(instance), Collection(instance), lang::Object(instance) {
    InitAbstractCollection();
}

AbstractCollection::~AbstractCollection() {}

bool AbstractCollection::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 0), e.instance);
}

bool AbstractCollection::AddAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 1), c.instance);
}

void AbstractCollection::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsAbstractCollection, 2));
}

bool AbstractCollection::Contains(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 3), o.instance);
}

bool AbstractCollection::ContainsAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 4), c.instance);
}

bool AbstractCollection::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 5));
}

Iterator AbstractCollection::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractCollection, 6)));
}

bool AbstractCollection::Remove(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 7), o.instance);
}

bool AbstractCollection::RemoveAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 8), c.instance);
}

bool AbstractCollection::RetainAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractCollection, 9), c.instance);
}

int AbstractCollection::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAbstractCollection, 10));
}

lang::Object* AbstractCollection::ToArray(int* size) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsAbstractCollection, 11)));
}

lang::Object* AbstractCollection::ToArray(int* size, lang::Object* a, int ac) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsAbstractCollection, 12), ToJObjectArray<lang::Object>(a, ac)));
}

char* AbstractCollection::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsAbstractCollection, 13)));
}


// java/util/List
// Name: List
// Base Class(es): [Collection]

NativeObject clsList;
jclass InitList() {
   return tcInitNativeObject(clsList, "java/util/List", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "add", "(ILjava/lang/Object;)V", false }, 
          { "addAll", "(Ljava/util/Collection;)Z", false }, 
          { "addAll", "(ILjava/util/Collection;)Z", false }, 
          { "clear", "()V", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsAll", "(Ljava/util/Collection;)Z", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "get", "(I)Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "indexOf", "(Ljava/lang/Object;)I", false }, 
          { "isEmpty", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "lastIndexOf", "(Ljava/lang/Object;)I", false }, 
          { "listIterator", "()Ljava/util/ListIterator;", false }, 
          { "listIterator", "(I)Ljava/util/ListIterator;", false }, 
          { "remove", "(I)Ljava/lang/Object;", false }, 
          { "remove", "(Ljava/lang/Object;)Z", false }, 
          { "removeAll", "(Ljava/util/Collection;)Z", false }, 
          { "retainAll", "(Ljava/util/Collection;)Z", false }, 
          { "set", "(ILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "size", "()I", false }, 
          { "subList", "(II)Ljava/util/List;", false }, 
          { "toArray", "()[Ljava/lang/Object;", false }, 
          { "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", false }
       }, { 
       });
}

List::List(jobject instance)
 : instance(instance), Collection(instance) {
    InitList();
}

List::~List() {}

bool List::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 0), e.instance);
}

void List::Add(int index, lang::Object element) {
   tc::jEnv->CallVoidMethod(METHOD(clsList, 1), (jint)index, element.instance);
}

bool List::AddAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 2), c.instance);
}

bool List::AddAll(int index, Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 3), (jint)index, c.instance);
}

void List::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsList, 4));
}

bool List::Contains(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 5), o.instance);
}

bool List::ContainsAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 6), c.instance);
}

bool List::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 7), o.instance);
}

lang::Object List::Get(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsList, 8), (jint)index));
}

int List::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsList, 9));
}

int List::IndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsList, 10), o.instance);
}

bool List::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 11));
}

Iterator List::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsList, 12)));
}

int List::LastIndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsList, 13), o.instance);
}

ListIterator List::ListIterator0() {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsList, 14)));
}

ListIterator List::ListIterator0(int index) {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsList, 15), (jint)index));
}

lang::Object List::Remove(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsList, 16), (jint)index));
}

bool List::Remove(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 17), o.instance);
}

bool List::RemoveAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 18), c.instance);
}

bool List::RetainAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsList, 19), c.instance);
}

lang::Object List::Set(int index, lang::Object element) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsList, 20), (jint)index, element.instance));
}

int List::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsList, 21));
}

List List::SubList(int fromIndex, int toIndex) {
   return (List)(tc::jEnv->CallObjectMethod(METHOD(clsList, 22), (jint)fromIndex, (jint)toIndex));
}

lang::Object* List::ToArray(int* size) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsList, 23)));
}

lang::Object* List::ToArray(int* size, lang::Object* a, int ac) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsList, 24), ToJObjectArray<lang::Object>(a, ac)));
}


// java/util/AbstractList
// Name: AbstractList
// Base Class(es): [List, AbstractCollection]

NativeObject clsAbstractList;
jclass InitAbstractList() {
   return tcInitNativeObject(clsAbstractList, "java/util/AbstractList", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "add", "(ILjava/lang/Object;)V", false }, 
          { "addAll", "(ILjava/util/Collection;)Z", false }, 
          { "clear", "()V", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "get", "(I)Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "indexOf", "(Ljava/lang/Object;)I", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "lastIndexOf", "(Ljava/lang/Object;)I", false }, 
          { "listIterator", "()Ljava/util/ListIterator;", false }, 
          { "listIterator", "(I)Ljava/util/ListIterator;", false }, 
          { "remove", "(I)Ljava/lang/Object;", false }, 
          { "removeRange", "(II)V", false }, 
          { "set", "(ILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "subList", "(II)Ljava/util/List;", false }
       }, { 
       });
}

AbstractList::AbstractList(jobject instance)
 : instance(instance), List(instance), AbstractCollection(instance) {
    InitAbstractList();
}

AbstractList::~AbstractList() {}

bool AbstractList::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractList, 0), e.instance);
}

void AbstractList::Add(int index, lang::Object element) {
   tc::jEnv->CallVoidMethod(METHOD(clsAbstractList, 1), (jint)index, element.instance);
}

bool AbstractList::AddAll(int index, Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractList, 2), (jint)index, c.instance);
}

void AbstractList::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsAbstractList, 3));
}

bool AbstractList::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAbstractList, 4), o.instance);
}

lang::Object AbstractList::Get(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 5), (jint)index));
}

int AbstractList::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAbstractList, 6));
}

int AbstractList::IndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAbstractList, 7), o.instance);
}

Iterator AbstractList::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 8)));
}

int AbstractList::LastIndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAbstractList, 9), o.instance);
}

ListIterator AbstractList::ListIterator0() {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 10)));
}

ListIterator AbstractList::ListIterator0(int index) {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 11), (jint)index));
}

lang::Object AbstractList::Remove(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 12), (jint)index));
}

void AbstractList::RemoveRange(int fromIndex, int toIndex) {
   tc::jEnv->CallVoidMethod(METHOD(clsAbstractList, 13), (jint)fromIndex, (jint)toIndex);
}

lang::Object AbstractList::Set(int index, lang::Object element) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 14), (jint)index, element.instance));
}

List AbstractList::SubList(int fromIndex, int toIndex) {
   return (List)(tc::jEnv->CallObjectMethod(METHOD(clsAbstractList, 15), (jint)fromIndex, (jint)toIndex));
}


// java/util/Vector
// Name: Vector
// Base Class(es): [AbstractList]

NativeObject clsVector;
jclass InitVector() {
   return tcInitNativeObject(clsVector, "java/util/Vector", { 
          { "add", "(Ljava/lang/Object;)Z", false }, 
          { "add", "(ILjava/lang/Object;)V", false }, 
          { "addAll", "(Ljava/util/Collection;)Z", false }, 
          { "addAll", "(ILjava/util/Collection;)Z", false }, 
          { "addElement", "(Ljava/lang/Object;)V", false }, 
          { "capacity", "()I", false }, 
          { "clear", "()V", false }, 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsAll", "(Ljava/util/Collection;)Z", false }, 
          { "copyInto", "([Ljava/lang/Object;)V", false }, 
          { "elementAt", "(I)Ljava/lang/Object;", false }, 
          { "elements", "()Ljava/util/Enumeration;", false }, 
          { "ensureCapacity", "(I)V", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "firstElement", "()Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "indexOf", "(Ljava/lang/Object;)I", false }, 
          { "indexOf", "(Ljava/lang/Object;I)I", false }, 
          { "insertElementAt", "(Ljava/lang/Object;I)V", false }, 
          { "isEmpty", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "lastElement", "()Ljava/lang/Object;", false }, 
          { "lastIndexOf", "(Ljava/lang/Object;)I", false }, 
          { "lastIndexOf", "(Ljava/lang/Object;I)I", false }, 
          { "listIterator", "()Ljava/util/ListIterator;", false }, 
          { "listIterator", "(I)Ljava/util/ListIterator;", false }, 
          { "remove", "(I)Ljava/lang/Object;", false }, 
          { "remove", "(Ljava/lang/Object;)Z", false }, 
          { "removeAll", "(Ljava/util/Collection;)Z", false }, 
          { "removeAllElements", "()V", false }, 
          { "removeElement", "(Ljava/lang/Object;)Z", false }, 
          { "removeElementAt", "(I)V", false }, 
          { "removeRange", "(II)V", false }, 
          { "retainAll", "(Ljava/util/Collection;)Z", false }, 
          { "set", "(ILjava/lang/Object;)Ljava/lang/Object;", false }, 
          { "setElementAt", "(Ljava/lang/Object;I)V", false }, 
          { "setSize", "(I)V", false }, 
          { "size", "()I", false }, 
          { "subList", "(II)Ljava/util/List;", false }, 
          { "toArray", "()[Ljava/lang/Object;", false }, 
          { "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "trimToSize", "()V", false }
       }, { 
       });
}

Vector::Vector(jobject instance)
 : instance(instance), AbstractList(instance) {
    InitVector();
}

Vector::~Vector() {}

Vector::Vector()
 : instance(tc::jEnv->NewObject(InitVector(), tc::jEnv->GetMethodID(InitVector(), "<init>", "()V"))), AbstractList(instance)
{}

Vector::Vector(Collection c)
 : instance(tc::jEnv->NewObject(InitVector(), tc::jEnv->GetMethodID(InitVector(), "<init>", "(Ljava/util/Collection;)V"), c.instance)), AbstractList(instance)
{}

Vector::Vector(int initialCapacity)
 : instance(tc::jEnv->NewObject(InitVector(), tc::jEnv->GetMethodID(InitVector(), "<init>", "(I)V"), (jint)initialCapacity)), AbstractList(instance)
{}

Vector::Vector(int initialCapacity, int capacityIncrement)
 : instance(tc::jEnv->NewObject(InitVector(), tc::jEnv->GetMethodID(InitVector(), "<init>", "(II)V"), (jint)initialCapacity, (jint)capacityIncrement)), AbstractList(instance)
{}

bool Vector::Add(lang::Object e) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 0), e.instance);
}

void Vector::Add(int index, lang::Object e) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 1), (jint)index, e.instance);
}

bool Vector::AddAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 2), c.instance);
}

bool Vector::AddAll(int index, Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 3), (jint)index, c.instance);
}

void Vector::AddElement(lang::Object obj) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 4), obj.instance);
}

int Vector::Capacity() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 5));
}

void Vector::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 6));
}

lang::Object Vector::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 7)));
}

bool Vector::Contains(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 8), o.instance);
}

bool Vector::ContainsAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 9), c.instance);
}

void Vector::CopyInto(lang::Object* anArray, int anArrayc) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 10), ToJObjectArray<lang::Object>(anArray, anArrayc));
}

lang::Object Vector::ElementAt(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 11), (jint)index));
}

Enumeration Vector::Elements() {
   return (Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 12)));
}

void Vector::EnsureCapacity(int minCapacity) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 13), (jint)minCapacity);
}

bool Vector::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 14), o.instance);
}

lang::Object Vector::FirstElement() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 15)));
}

int Vector::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 16));
}

int Vector::IndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 17), o.instance);
}

int Vector::IndexOf(lang::Object o, int index) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 18), o.instance, (jint)index);
}

void Vector::InsertElementAt(lang::Object obj, int index) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 19), obj.instance, (jint)index);
}

bool Vector::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 20));
}

Iterator Vector::Iterator0() {
   return (Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 21)));
}

lang::Object Vector::LastElement() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 22)));
}

int Vector::LastIndexOf(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 23), o.instance);
}

int Vector::LastIndexOf(lang::Object o, int index) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 24), o.instance, (jint)index);
}

ListIterator Vector::ListIterator0() {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 25)));
}

ListIterator Vector::ListIterator0(int index) {
   return (ListIterator)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 26), (jint)index));
}

lang::Object Vector::Remove(int index) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 27), (jint)index));
}

bool Vector::Remove(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 28), o.instance);
}

bool Vector::RemoveAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 29), c.instance);
}

void Vector::RemoveAllElements() {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 30));
}

bool Vector::RemoveElement(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 31), obj.instance);
}

void Vector::RemoveElementAt(int index) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 32), (jint)index);
}

void Vector::RemoveRange(int fromIndex, int toIndex) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 33), (jint)fromIndex, (jint)toIndex);
}

bool Vector::RetainAll(Collection c) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsVector, 34), c.instance);
}

lang::Object Vector::Set(int index, lang::Object element) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 35), (jint)index, element.instance));
}

void Vector::SetElementAt(lang::Object obj, int index) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 36), obj.instance, (jint)index);
}

void Vector::SetSize(int newSize) {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 37), (jint)newSize);
}

int Vector::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsVector, 38));
}

List Vector::SubList(int fromIndex, int toIndex) {
   return (List)(tc::jEnv->CallObjectMethod(METHOD(clsVector, 39), (jint)fromIndex, (jint)toIndex));
}

lang::Object* Vector::ToArray(int* size) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsVector, 40)));
}

lang::Object* Vector::ToArray(int* size, lang::Object* a, int ac) {
   return FromJObjectArray<lang::Object>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsVector, 41), ToJObjectArray<lang::Object>(a, ac)));
}

char* Vector::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsVector, 42)));
}

void Vector::TrimToSize() {
   tc::jEnv->CallVoidMethod(METHOD(clsVector, 43));
}


// java/util/Dictionary
// Name: Dictionary
// Base Class(es): [lang::Object]

NativeObject clsDictionary;
jclass InitDictionary() {
   return tcInitNativeObject(clsDictionary, "java/util/Dictionary", { 
          { "elements", "()Ljava/util/Enumeration;", false }, 
          { "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "isEmpty", "()Z", false }, 
          { "keys", "()Ljava/util/Enumeration;", false }, 
          { "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "size", "()I", false }
       }, { 
       });
}

Dictionary::Dictionary(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitDictionary();
}

Dictionary::~Dictionary() {}

Enumeration Dictionary::Elements() {
   return (Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsDictionary, 0)));
}

lang::Object Dictionary::Get(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsDictionary, 1), key.instance));
}

bool Dictionary::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsDictionary, 2));
}

Enumeration Dictionary::Keys() {
   return (Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsDictionary, 3)));
}

lang::Object Dictionary::Put(lang::Object key, lang::Object value) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsDictionary, 4), key.instance, value.instance));
}

lang::Object Dictionary::Remove(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsDictionary, 5), key.instance));
}

int Dictionary::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsDictionary, 6));
}


// java/util/Hashtable
// Name: Hashtable
// Base Class(es): [Map, Dictionary]

NativeObject clsHashtable;
jclass InitHashtable() {
   return tcInitNativeObject(clsHashtable, "java/util/Hashtable", { 
          { "clear", "()V", false }, 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "contains", "(Ljava/lang/Object;)Z", false }, 
          { "containsKey", "(Ljava/lang/Object;)Z", false }, 
          { "containsValue", "(Ljava/lang/Object;)Z", false }, 
          { "elements", "()Ljava/util/Enumeration;", false }, 
          { "entrySet", "()Ljava/util/Set;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "hashCode", "()I", false }, 
          { "isEmpty", "()Z", false }, 
          { "keys", "()Ljava/util/Enumeration;", false }, 
          { "keySet", "()Ljava/util/Set;", false }, 
          { "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "putAll", "(Ljava/util/Map;)V", false }, 
          { "rehash", "()V", false }, 
          { "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false }, 
          { "size", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "values", "()Ljava/util/Collection;", false }
       }, { 
       });
}

Hashtable::Hashtable(jobject instance)
 : instance(instance), Map(instance), Dictionary(instance) {
    InitHashtable();
}

Hashtable::~Hashtable() {}

Hashtable::Hashtable()
 : instance(tc::jEnv->NewObject(InitHashtable(), tc::jEnv->GetMethodID(InitHashtable(), "<init>", "()V"))), Map(instance), Dictionary(instance)
{}

Hashtable::Hashtable(int initialCapacity)
 : instance(tc::jEnv->NewObject(InitHashtable(), tc::jEnv->GetMethodID(InitHashtable(), "<init>", "(I)V"), (jint)initialCapacity)), Map(instance), Dictionary(instance)
{}

Hashtable::Hashtable(int initialCapacity, float loadFactor)
 : instance(tc::jEnv->NewObject(InitHashtable(), tc::jEnv->GetMethodID(InitHashtable(), "<init>", "(IF)V"), (jint)initialCapacity, (jfloat)loadFactor)), Map(instance), Dictionary(instance)
{}

Hashtable::Hashtable(Map t)
 : instance(tc::jEnv->NewObject(InitHashtable(), tc::jEnv->GetMethodID(InitHashtable(), "<init>", "(Ljava/util/Map;)V"), t.instance)), Map(instance), Dictionary(instance)
{}

void Hashtable::Clear() {
   tc::jEnv->CallVoidMethod(METHOD(clsHashtable, 0));
}

lang::Object Hashtable::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 1)));
}

bool Hashtable::Contains(lang::Object value) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsHashtable, 2), value.instance);
}

bool Hashtable::ContainsKey(lang::Object key) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsHashtable, 3), key.instance);
}

bool Hashtable::ContainsValue(lang::Object value) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsHashtable, 4), value.instance);
}

Enumeration Hashtable::Elements() {
   return (Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 5)));
}

Set Hashtable::EntrySet() {
   return (Set)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 6)));
}

bool Hashtable::Equals(lang::Object o) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsHashtable, 7), o.instance);
}

lang::Object Hashtable::Get(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 8), key.instance));
}

int Hashtable::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsHashtable, 9));
}

bool Hashtable::IsEmpty() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsHashtable, 10));
}

Enumeration Hashtable::Keys() {
   return (Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 11)));
}

Set Hashtable::KeySet() {
   return (Set)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 12)));
}

lang::Object Hashtable::Put(lang::Object key, lang::Object value) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 13), key.instance, value.instance));
}

void Hashtable::PutAll(Map t) {
   tc::jEnv->CallVoidMethod(METHOD(clsHashtable, 14), t.instance);
}

void Hashtable::Rehash() {
   tc::jEnv->CallVoidMethod(METHOD(clsHashtable, 15));
}

lang::Object Hashtable::Remove(lang::Object key) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 16), key.instance));
}

int Hashtable::Size() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsHashtable, 17));
}

char* Hashtable::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 18)));
}

Collection Hashtable::Values() {
   return (Collection)(tc::jEnv->CallObjectMethod(METHOD(clsHashtable, 19)));
}


// java/util/Random
// Name: Random
// Base Class(es): [lang::Object]

NativeObject clsRandom;
jclass InitRandom() {
   return tcInitNativeObject(clsRandom, "java/util/Random", { 
          { "nextBoolean", "()Z", false }, 
          { "nextBytes", "([B)V", false }, 
          { "nextDouble", "()D", false }, 
          { "nextFloat", "()F", false }, 
          { "nextGaussian", "()D", false }, 
          { "nextInt", "()I", false }, 
          { "nextInt", "(I)I", false }, 
          { "nextLong", "()J", false }, 
          { "setSeed", "(J)V", false }
       }, { 
       });
}

Random::Random(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitRandom();
}

Random::~Random() {}

Random::Random()
 : instance(tc::jEnv->NewObject(InitRandom(), tc::jEnv->GetMethodID(InitRandom(), "<init>", "()V"))), lang::Object(instance)
{}

Random::Random(long seed)
 : instance(tc::jEnv->NewObject(InitRandom(), tc::jEnv->GetMethodID(InitRandom(), "<init>", "(J)V"), (jlong)seed)), lang::Object(instance)
{}

bool Random::NextBoolean() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsRandom, 0));
}

void Random::NextBytes(signed char* chars, int charsc) {
   tc::jEnv->CallVoidMethod(METHOD(clsRandom, 1), ToJByteArray(chars, charsc));
}

double Random::NextDouble() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRandom, 2));
}

float Random::NextFloat() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsRandom, 3));
}

double Random::NextGaussian() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsRandom, 4));
}

int Random::NextInt() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRandom, 5));
}

int Random::NextInt(int n) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsRandom, 6), (jint)n);
}

long Random::NextLong() {
   return (long)tc::jEnv->CallLongMethod(METHOD(clsRandom, 7));
}

void Random::SetSeed(long seed) {
   tc::jEnv->CallVoidMethod(METHOD(clsRandom, 8), (jlong)seed);
}


