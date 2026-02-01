package com.leonardower.mymovie.ui.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.ui.components.tiles.genre.GenreTile

@Composable
fun GenreList(
    allGenres: List<Genre>,
    onGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.genres),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        items(allGenres) { genre ->
            GenreTile(
                genre = genre,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGenreClick(genre.id) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GenreListPreview() {
    GenreList(
        allGenres = listOf(
            Genre(1, "Фантастика", iconUrl = "https://kinopoisk-ru.clstorage.net/1V4vG3239/041877ElbfNn/YEPDUCDyYnmDvVgLFTs-gdAoI-BuzL1psz3YsMtSsj3py74xcliNZVHzzo_z4NmqUs3ZYle9MxBSrehAsdhiEvRmV1EEV7dIkhlNe7BPmhCl6fexC58f33EOq6KIIQS6Li_KJUJICGDUUpsPcoV4XjbOarPVqRBnsfmNFPEswpcTgYNgJGPjusX8aAsUSG3kQ9sx5m2-3E2x3jlDK2M2O_-MenbUGcpwxgMpn3PX_TyV32pltZqidSCKnEeCnrKHY8Kj4abS8Xk3_EopRFnOJQP4YFW-3mw_c10IESoh9Cmub4hUtDrI4Hei6ZzB9byuN31rBlX6oeVhut-HcM6BJSBjE9M1cpDYhA566STbTlVD2TJWXR2M7dOP-mN544XqPD7IdQTo2gCWgwjtQKXd7Cacm7eXmiOFQLptFuPPQjUTowJCJrJimCfuO4in2D9VQHijBw0eLA5CXflRO6IE6Oyu2xW0KlgCBMDLnaCEXY333Bn0p0jAhcNo7RZR_OLXM_JQkAcwc5nnrplJZlk9NSBZ8GRM_B-Ns74I8zvAt5gOD9s0tniaQLbzCQ0CtPyuJNy4Bvc4MqdCaZwXct3yxWGxABJE0gMrlAyouwTrfdUASMKG7bx8nnB_yDP4A0SYfv7ZhrX56EJmsrnt0reNnwVc6kbmy2NmMPndtVCfI6YiARAAlVIDe8V9WwuHWC_3k-ixlB38Ds_QTYsSiXLU6m1vu-bkeMuA1CGIvfPFfN3FLorWVcjiRVDKDQbx34F1YcNSk1bgUSom_Hq71mkuJmC7UGY8Xj58Myy6wKhhBpguLbpG9ggrw0RTO37CFe2flK-qdYU6weYySe5Xop2w1jNAQNGVEFCqZg1riTcKfeYgeWHH3j3dvwPsKLCbYOdZPL45t1aYOOB387r9wvbt3ZbPaubUmLB1Qbr910NuchYxw1BDR3PDCHQ-KLmmOT1148gBBR_OzB8zb6oSmFPXWjyeezYHivljJDDJfXK3X-62H2h258vyhqL4PhcTbwN2EHNDY4QjsRrH3Npr5fo-V0JKQ-ef_a7s85z4YphgtGmMnlnVt9rJU0ZDytyzJJztBI3YNIR4IAeAO_3Hwp0iJnMhQWJEgmFrFU-buVQLTxSgSxJXDZxeX5C-KGLZURdojl0r1af4WfNF0fsu4iWNHAftahRH-uPGYaj9V3KfcMeRMHHxxJIBelccqmkFaY4loFrglI6dTb4wbmqheGL3yP4fGFc0GluwRZOIntCFHT3kHyk2dpqBxzIazVTBHTK1w_Dy87ZiMCukXWtYhupcVfIq0MT-Hqwdsry6oykjBjs8XZs1J7jIwCeQus9iJp6NhD861XV6kSehyK81od3Q50HywmKFQMF5FhwYO5cpzdZiiXOk7739PxIOKNK789Y4nt85tyRqiHIGYMi8wtbuXIa8-nSlGtKXcfhPt3IMc0UjU4Cx5YCTayU-ismkSG6Fo4kwJn5MPV9yX_szGRLF2N_dylU3uynzlNEZnYI1fP6GHRlWtwsjxGObHTWxXZCUciNz4jRAcBqW3oiqlBuchKFbMBS__X7Pg_2J8Mnw9DgtvZvWFHgo4Ufwuz9B1W48hd04NHcL0nZDmh2U843ydCIj8DP2kYAYFwyLWlTrDhRASmBXzb7M36CeqMI7cURqzb_b1aTYmrNmcjkO0qaebff-OsSF6-JXMKrcNJMugwRykYDgdDLzqlYviXpW-00XQOvy957sDq9RnplwKBIV2u3-KeSkCugQBzEprZHlvN6Uz8u21YqSBzPYb4Rw3TJFEfFiQaVD4ph2PBpa9-uMljL58TcM_fx_Uiz4wtuiFVrcr5nFJnk4EBbzCu6wlK-f9t-rpOW7M4dgOf9UoT8hFkORgNP3MoG59G2bKRVLXCWCGDOmjA6_b5APKEBL0odJPBxKJEb4-FBHk_m-4hXfLZUsuDX1iJBWQ-vNl5P_kIegYULRh0Ey6ad86PukW5yVIvtBJdwefM1DnJnRSgDViJ-s-iU1yYuiBAEaX3MW_vw1f9g3pqtwpEKb3HUgvIFG8xFRIAWwoGj3DvoJlEg9pFHpAAY__o8u4g7aITlgpCve37o01cmqAGTQiH7zZP--NO9qFud5UoQiuu02wK2QdgIwwgNE4sGqBg9oy7Ua_oXg2-PkfO3-vXFeWmFbg7XK_537JJeLOiKF0Kuew5f_HEcdy4THeQBUINqfROD80QTRYYCDNRJCGAcuSkim-RylIUqy5E3NbB2RbKlBCKLWCKwfORbX-KqDBfK5bMFW7H5F3yhXBzjyFmCbzhZS3zBlA8GgoeZDw3u1_WrJl8sdNmGI49cezl0dMF_pcViBNcufzPpWZOip4AYj25-wpR2fpV05V5WLc_cSKe-GcY7DVUHjAUHkoNG59c67O3dbjGRSmODUvdxu3QIeGrDLYrSaXU7LtNW6SDMW06iegzXsTYVeCNR2e3E0EZq_pSFOo2WSUZIiBTBSm4Qv-Nt1aV3GAltyZQ_ubv8zfaixCBH2CH1tGSYU2ipiVHK7v6Hkzg9XzDvUhVqj18Cqf_WxDIOXgkBzMTRxEhgXLHqJZzocddD6A6as3owMAkwI0FiR55vubdtmpZrp4negGn_TNo4dRf76BndpQ-Xj6J-Vcx3DhHABwDNnApEI502YaMUpf6Yx-lPE3SwNb-I9CIIKAvbrjD7p5vYp2DNnoqrdc4WMjTcdqfWlOqCkAMos9PP-wEeyU4AQFNGQCDfs2FikWu12cttyRf8cDt5xrrsCy-M0e91M6aSlO_mBt_K6fyCln99Fzcp0dbrzhpGpvcTB3IMWYgFigQUCUSok_PkZVRncpHKrQ8U-Ljw_ET7acvlghepc7jl0VwpaIzejSv9DlT3NR674xff4k3fxSD_3s9-QxABgouBGsoCLla74ydc4TpVSC3JE3b_cHiOsuMHbohVr7O6qdxY5-sJE00je8qWuDUSNu4TVOVGl8"),
            Genre(2, "Триллер", iconUrl = "https://example.com/thriller.jpg"),
            Genre(3, "Драма", iconUrl = "https://example.com/drama.jpg"),
            Genre(4, "Комедия", iconUrl = "https://example.com/comedy.jpg"),
            Genre(5, "Аниме", iconUrl = "https://example.com/anime.jpg"),
            Genre(6, "Ужасы", iconUrl = "https://example.com/horror.jpg"),
            Genre(7, "Боевик", iconUrl = "https://example.com/action.jpg"),
            Genre(8, "Криминал", iconUrl = "https://example.com/crime.jpg"),
            Genre(9, "Мелодрама", iconUrl = "https://example.com/romance.jpg"),
            Genre(10, "Приключения", iconUrl = "https://example.com/adventure.jpg")
        ),
        onGenreClick = {},
    )
}