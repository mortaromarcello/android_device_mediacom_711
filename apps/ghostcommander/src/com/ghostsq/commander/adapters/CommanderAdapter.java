package com.ghostsq.commander.adapters;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.ghostsq.commander.Commander;
import com.ghostsq.commander.utils.Credentials;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.widget.AdapterView;

/**
 * <code>CommanderAdapter</code> interface
 * @author Ghost Squared (ghost.sq2@gmail.com)
 * 
 * All the adapters should extend {@link CommanderAdapterBase}
 * which implements this interface.
 *
 */
public interface CommanderAdapter {

    /**
     *   
     *   "Object ListAdapter.getItem( int position )" returns an instance of the following class:  
     */
    public class Item {
        public  String    name = "";
        public  Date      date = null;
        public  long      size = -1;
        public  boolean   dir = false, sel = false;
        public  String    attr = "";
        public  Object    origin = null;
        public  int       icon_id = -1;
        private Drawable  thumbnail;
        private long      thumbnailUsed;
        public  int       colorCache = 0;
        public  Item() {}
        public  Item( String name_ )    { name = name_; }
        public  final boolean  isThumbNail()  { return thumbnail != null; }
        public  final Drawable getThumbNail()             { thumbnailUsed = System.currentTimeMillis(); return thumbnail; }
        public  final void     setThumbNail( Drawable t ) { thumbnailUsed = System.currentTimeMillis(); thumbnail = t; }
        public  final void     setIcon( Drawable t ) { setThumbNail( t ); thumb_is_icon = true; }
        public  final boolean  remThumbnailIfOld( int ttl ) { 
            if( thumbnail != null && !need_thumb && System.currentTimeMillis() - thumbnailUsed > ttl ) {
                thumbnail = null;
                return true;
            }
            return false;
        }
        public boolean  need_thumb = false, no_thumb = false, thumb_is_icon = false;
    }
    
    /**
	 * @param c - only the default constructor can be called of a foreign loaded class, so we pass the reference to commander here  
	 */
	public void Init( Commander c );

    /**
     * @param uri - the URI of the resource to connect to or work with  
     */
    public void setUri( Uri uri );
    
    /**
     * @return current adapter's source URI
     *      the URI returned always without the credentials. Use the getCredentials() to obtain  
     */
    public Uri getUri();
	
	/**
	 *  Output modes
	 */
	public final static int MODE_WIDTH = 0x0001, NARROW_MODE = 0x0000,     WIDE_MODE = 0x0001, 
	                      MODE_DETAILS = 0x0002, SIMPLE_MODE = 0x0000, DETAILED_MODE = 0x0002,
	                      MODE_FINGERF = 0x0004,   SLIM_MODE = 0x0000,      FAT_MODE = 0x0004,
                          MODE_HIDDEN  = 0x0008,   SHOW_MODE = 0x0000,     HIDE_MODE = 0x0008,
                          MODE_SORTING = 0x0030,   SORT_NAME = 0x0000,     SORT_SIZE = 0x0010, SORT_DATE = 0x0020, SORT_EXT = 0x0030,
                        MODE_SORT_DIR  = 0x0040,    SORT_ASC = 0x0000,      SORT_DSC = 0x0040,
                            MODE_CASE  = 0x0080,   CASE_SENS = 0x0000,   CASE_IGNORE = 0x0080,
                             MODE_ATTR = 0x0300,     NO_ATTR = 0x0000,     SHOW_ATTR = 0x0100, ATTR_ONLY = 0x0200,
                             MODE_ROOT = 0x0400,  BASIC_MODE = 0x0000,     ROOT_MODE = 0x0400,
                            MODE_ICONS = 0x3000,   TEXT_MODE = 0x0000,     ICON_MODE = 0x1000, ICON_TINY = 0x2000,
                             
                            LIST_STATE = 0x10000,  STATE_IDLE = 0x00000,  STATE_BUSY = 0x10000,
//                       SET_MODE_COLORS = 0xF0000000, SET_TXT_COLOR = 0x10000000, SET_SEL_COLOR = 0x20000000,
                          SET_TBN_SIZE = 0x01000000, SET_FONT_SIZE = 0x02000000;
    /**
     * @param mask - see the bits above 
     * @param mode - see the bits above 
     * @return the current mode 
     */
    public int setMode( int mask, int mode );
    /**
     *   <code>populateContextMenu</code> is called when the user taps and holds on an item
     *   @param menu - to call the method .add()
     *   @param acmi - to know which item is processed
     *   @param num  - current mode
     */
    public void populateContextMenu( ContextMenu menu, AdapterView.AdapterContextMenuInfo acmi, int num );

    /**
     * <code>getType</code> return the adapter implementation type
     * @return the adapter type bit {@link CA} 
     */
    public int getType();

    /**
     * This method is deprecated and to be removed. Use the setCredentials() instead 
     * @param name, pass 
     * @deprecated 
     */
    public void setIdentities( String name, String pass );

    /**
     * @param user credentials
     */
    public void setCredentials( Credentials crd );

    /**
     * @return user credentials
     */
    public Credentials getCredentials();
    
    /**
     * @param uri - a folder's URI to initialize. If null passed, just refresh
     * @param pass_back_on_done - the file name to select
     */
	public boolean readSource( Uri uri, String pass_back_on_done );

	/**
     *      Tries to do something with the item (Outside of an adapter we don't know how to process it).
     *      But an adapter knows, is it a folder and can be opened (it calls Commander.Navigate() in this case)
     *      or processed as default action (then it calls Commander.Open() )
	 * @param position index of the item to action
	 */
	public void openItem( int position );
	
    /**
     * @param position
     * @param full       - true - to return the absolute path, false - only the local name
     * @return string representation of the item
     */
    public String getItemName( int position, boolean full );    

    /**
     * @param position
     * @return full URI to access the item without the credentials!
     */
    public Uri getItemUri( int position );    

	/**
	 * @param  cis selected item (files or directories)
	 *         will call Commander.NotifyMe( "requested size info", Commander.OPERATION_COMPLETED ) when done  
	 */
	public void reqItemsSize( SparseBooleanArray cis );
	
	/**
	 * @param position in the list
     * @param newName for the item
     * @param copy file (preserve old name)
	 * @return true if success
	 */
	public boolean renameItem( int position, String newName, boolean copy );
	
	/**
	 * @param cis	booleans which internal items to copy
	 * @param to    an Adapter to be called {@link receiveItems()} and files to be passed
	 * @param move  move instead of copy
	 * @return      true if succeeded
	 */
	public boolean copyItems( SparseBooleanArray cis, CommanderAdapter to, boolean move );
	
    public final static int MODE_COPY = 0;
    public final static int MODE_MOVE = 1;
    public final static int MODE_DEL_SRC_DIR = 2;
    public final static int MODE_MOVE_DEL_SRC_DIR = 3;
	/**
	 * @param fileURIs  list of files as universal transport parcel. All kind of adapters (network, etc.)
	 * 					accepts data as files. It should be called from the current list's adapter
	 * @param move      move instead of copy
	 * @return          true if succeeded
	 */
	public boolean receiveItems( String[] fileURIs, int move_mode );

    /**
     * @param fileURI - the location of the file  
     * @return        - the Item with all the information in it
     */
    public Item getItem( Uri fileURI );

    /**
     * @param fileURI - the location of the file
     * @param skip    - tells the data provider to start from a middle point  
     *   
     * @return        - the content of the file
     */
    public InputStream getContent( Uri fileURI, long skip );

    /**
     *  same as getContent( fileURI, 0 );
     */
    public InputStream getContent( Uri fileURI );

    /**
     * @param fileURI - the location of the file
     * @return  stream to data be written 
     */
    public OutputStream saveContent( Uri fileURI );
    
    /**
     * @param s - the stream obtained by the getContent() or saveContent() methods to be closed by calling this  
     */
    public void closeStream( Closeable s );
    

    /**
     * @param fileURI - the location of the file to create  
     */
    public boolean createFile( String fileURI );

	public void createFolder( String string );

    /**
     * @param  cis selected item (files or directories)
     *         will call Commander.NotifyMe( "requested size info", Commander.OPERATION_COMPLETED ) when done  
     */
	public boolean deleteItems( SparseBooleanArray cis );
    
    /**
     * @param command_id - command id to execute
     * @param items - selected or checked items to work with  
     */
    public void doIt( int command_id, SparseBooleanArray cis );
    
    /**
     * to be called before the adapter is going to be destroyed
     */
	public void terminateOperation();
	public void prepareToDestroy();
}
