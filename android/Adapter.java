
String[] items_name = {"item1","item2","item3"};
int[] items_image = {R.drawable.sms, R.drawable.keep, R.drawable.dropbox};

ListView mListView = (ListView)FindViewById(R.id.listView);
MyAdapter myAdapter = new MyAdapter(this,items_name,items_image,R.layout.list_items);
mListView.setAdapter(myAdapter);

public class MyAdapter extends BaseAdapter {
	private Context mContext;
	private int mLayout;
	private String[] name;
	private int[] image;
	private LayoutInflater mLayoutInflater;

	public MyAdapter(Context context, String[] s, int[] i, int itemlayout){
		mContext = context;
		mLayout = itemlayout;
		name = s;
		image = i;
		mLayoutInflater = LayoutInflater.from(context);
	}
	// 创建这个是为了提升效率，一次查找对应视图和保存对应的指针，每次操作只需要从中读取指针地址就能操作视图中的控件
	public class ViewHolder{
		public TextView name;
		public ImageView image;

		public ViewHolder(View view){
			name = (TextView)view.findViewById(R.id.tv);
			image = (ImageView)view.findViewById(R.id.iv);
		}
	}
	// 返回position处的列表项的视图
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		View mView;

		if (convertView == null) {
			mView = mLayoutInflater.inflate(mLayout, null);
			convertView = mView;
			mViewHolder = new ViewHolder(mView);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder)convertView.getTag();
		}

		mViewHolder.name.setText(name[position]);
		mViewHolder.image.setImageResource(image[position]);

		return convertView;
	}
	// 有多少个列表项，很重要
	@Override
	public int getCount() {
		return name.length;
	}
	// 返回position处的列表项内容
	@Override
	public Object getItem(int position) {
		return null;
	}
	// 返回position处的列表项ID
	@Override
	public long getItemId(int position) {
		return position;
	}
}