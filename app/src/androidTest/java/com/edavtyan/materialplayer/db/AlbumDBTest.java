package com.edavtyan.materialplayer.db;

import android.support.test.runner.AndroidJUnit4;

import com.edavtyan.materialplayer.db.lib.AlbumDBHelper;
import com.edavtyan.materialplayer.db.lib.AlbumDBProvider;
import com.edavtyan.materialplayer.lib.BaseTest;
import com.edavtyan.materialplayer.lib.DBTest;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class AlbumDBTest extends DBTest {
	private static AlbumDB albumDB;
	private static AlbumDBHelper albumDBHelper;

	@BeforeClass
	public static void beforeClass() {
		BaseTest.beforeClass();
		initProvider(AlbumDBProvider.class);
		albumDB = new AlbumDB(context);
		albumDBHelper = new AlbumDBHelper(context);
	}

	@After
	public void afterEach() {
		albumDBHelper.reset();
	}

	@Test
	public void getAllAlbums_allArtistsOnDevice() {
		albumDBHelper.addRandomAlbums(10);
		assertThat(albumDB.getAllAlbums())
				.hasSize(10)
				.isSortedAccordingTo((lhs, rhs) -> lhs.getTitle().compareTo(rhs.getTitle()));
	}

	@Test
	public void getAlbumsWithArtistTitle_albumsWithSpecifiedTitle() {
		List<Album> albums = albumDBHelper.addRandomAlbumsWhereSomeWithArtistTitle(10, "title", 4);
		assertThat(albumDB.getAlbumsWithArtistTitle("title"))
				.hasSize(4)
				.containsAll(albums);

	}

	@Test
	public void getAlbumWithAlbumId_albumWithSpecifiedId() {
		Album album = new Album();
		album.setId(3);
		album.setTitle("title");
		album.setArtistTitle("artist");
		album.setTracksCount(9);
		album.setArt("art");

		albumDBHelper.addAlbum(album);

		Album albumFromDB = albumDB.getAlbumWithAlbumId(3);

		assertThat(albumFromDB.getId()).isEqualTo(3);
		assertThat(albumFromDB.getTitle()).isEqualTo("title");
		assertThat(albumFromDB.getArtistTitle()).isEqualTo("artist");
		assertThat(albumFromDB.getTracksCount()).isEqualTo(9);
		assertThat(albumFromDB.getArt()).isEqualTo("art");
	}
}