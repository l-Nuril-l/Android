package com.example.email

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.email.databinding.FragmentEmailListBinding
import com.example.email.databinding.ListItemEmailBinding
import com.example.email.databinding.ListItemImportantEmailBinding
import java.util.*
import javax.security.auth.callback.Callback

class EmailListFragment : Fragment() {

    //private lateinit var emailRecyclerView: RecyclerView
    //private lateinit var eButton: Button
    //private lateinit var eTextView: TextView
    private var adapter: EmailAdapter? = EmailAdapter(emptyList())
    private var callbacks: Callbacks? = null
    private lateinit var binding:FragmentEmailListBinding

    private val vm: EmailListViewModel by lazy {
        ViewModelProvider(this).get(EmailListViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val b = DataBindingUtil.inflate<FragmentEmailListBinding>(inflater,R.layout.fragment_email_list,container,false);
        binding = b;
        //val view = inflater.inflate(R.layout.fragment_email_list,container,false);

        b.emailRecyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
        //emailRecyclerView = b.emailRecyclerView

        //emailRecyclerView = view.findViewById(R.id.email_recycler_view)
        //emailRecyclerView.layoutManager = LinearLayoutManager(context)
        //emailRecyclerView.adapter = adapter



        //eTextView = b.tvEmpty
        //eTextView = view.findViewById(R.id.tv_empty)
        //eButton = b.bEmpty
        //eButton = view.findViewById(R.id.b_empty)


        return b.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(emails : List<Email>)
    {
        binding.bEmpty.isVisible = emails.isEmpty()
        binding.tvEmpty.isVisible = emails.isEmpty()
        binding.emailRecyclerView.isVisible = emails.isNotEmpty()
        if(emails.isEmpty()) Toast.makeText(context,"Список пуст!",Toast.LENGTH_SHORT).show()

        adapter?.setEmailList(emails)
        //emailRecyclerView.adapter = adapter;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_email_list,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.add_email -> {
                val email = Email()
                vm.addEmail(email)
                callbacks?.onEmailSelected(email.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.emailListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { tasks -> tasks?.let {
                updateUI(tasks)
            }})
        binding.bEmpty.setOnClickListener {
            val email = Email()
            vm.addEmail(email)
            callbacks?.onEmailSelected(email.id)
        }
    }

    private inner class EmailHolder(private val view: ViewDataBinding) : RecyclerView.ViewHolder(view.root), View.OnClickListener {
        //val senderTextView : TextView = itemView.findViewById(R.id.sender)
        //val titleTextView : TextView = itemView.findViewById(R.id.title)
        //val textTextView : TextView = itemView.findViewById(R.id.text)
        //val dateTextView : TextView = itemView.findViewById(R.id.date)

        init {
            if(view is ListItemEmailBinding)
            {
                view.viewModel = EmailViewModel();
            }
            if(view is ListItemImportantEmailBinding)
            {
                view.viewModel = EmailViewModel();
            }
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SimpleDateFormat")
        fun bind(email: Email) {

            if(view is ListItemEmailBinding)
            {
                view.viewModel?.email = email
            }
            if(view is ListItemImportantEmailBinding)
            {
                view.viewModel?.email = email
            }


        }

        override fun onClick(p0: View?) {
            if(view is ListItemEmailBinding)
            {
                callbacks?.onEmailSelected(view.viewModel?.id!!)
            }
            if(view is ListItemImportantEmailBinding)
            {
                callbacks?.onEmailSelected(view.viewModel?.id!!)
            }
        }


    }

    interface Callbacks {
        fun onEmailSelected(emailId: UUID)
    }

    private inner class EmailAdapter(var emails: List<Email>):RecyclerView.Adapter<EmailHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailHolder {

            val b = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                if (viewType == 1) R.layout.list_item_important_email else R.layout.list_item_email,
                parent,false);

//            val v: View = layoutInflater
//                .inflate(
//                    if (viewType == 1) R.layout.list_item_email else R.layout.list_item_important_email,
//                    parent,
//                    false
//                )
            return EmailHolder(b)
        }

        override fun onBindViewHolder(holder: EmailHolder, position: Int) {
            val email = emails[position]
            holder.bind(email)
        }

        override fun getItemCount(): Int {
            return emails.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (emails[position].isImportant) 1 else 0
        }

        fun setEmailList(newEmailList: List<Email>){
            val diffUtil = EmailDiffUtil(emails,newEmailList)
            val diffRes = DiffUtil.calculateDiff(diffUtil)
            emails = newEmailList
            diffRes.dispatchUpdatesTo(this)
        }
    }

    companion object
    {
        fun  newInstance() : EmailListFragment
        {
            return EmailListFragment();
        }
    }
}