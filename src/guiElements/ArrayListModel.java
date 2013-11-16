package guiElements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class ArrayListModel<E> extends AbstractListModel<E> implements
		List<E>
{
	private ArrayList<E> list;
	
	public ArrayListModel()
	{
		list = new ArrayList<E>();
	}

	@Override
	public E getElementAt(int index)
	{
		return list.get(index);
	}

	@Override
	public int getSize()
	{
		return list.size();
	}

	@Override
	public boolean add(E e)
	{
		if (list.add(e))
		{
			fireIntervalAdded(this, list.size() - 1, list.size() - 1);
			return true;
		}
		else
			return false;
	}

	@Override
	public void add(int index, E element)
	{
		list.add(index, element);
		fireIntervalAdded(this, index, index);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if (list.addAll(c))
		{
			if (!this.isEmpty())
				fireIntervalAdded(this, list.size() - c.size(), list.size() - 1);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if (list.addAll(index, c))
		{
			if (!this.isEmpty())
				fireIntervalAdded(this, index, index + c.size());
			return true;
		}
		else
			return false;
	}

	@Override
	public void clear()
	{
		int last = list.size() - 1;
		list.clear();
		if (last >= 0)
			fireIntervalRemoved(this, 0, last);
	}

	@Override
	public boolean contains(Object o)
	{
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return list.containsAll(c);
	}

	@Override
	public E get(int index)
	{
		return list.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object o)
	{
		int index = list.indexOf(o);
		if (list.remove(o))
		{
			fireIntervalRemoved(this, index, index);
			return true;
		}
		else
			return false;
	}

	@Override
	public E remove(int index)
	{
		E e = list.remove(index);
		fireIntervalRemoved(this, index, index);
		return e;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		int last = list.size() - 1;
		if (removeAll(c))
		{
			if (last >= 0)
				fireIntervalRemoved(this, 0, last);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		int last = list.size() - 1;
		if (retainAll(c))
		{
			if (last >= 0)
				fireIntervalRemoved(this, 0, last);
			return true;
		}
		else
			return false;
	}

	@Override
	public E set(int index, E element)
	{
		E e = list.set(index, element);
		fireContentsChanged(this, index, index);
		return e;
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return list.toArray(a);
	}

	public void notifyChanged(int index)
	{
		fireContentsChanged(this, index, index);
	}
}
