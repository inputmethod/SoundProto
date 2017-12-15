package com.typany.skin2.storage;

import com.typany.skin2.home.model.SkinBundle;
import com.typany.skin2.home.model.SkinCategory;
import com.typany.skin2.home.model.SkinCategoryGroup;
import com.typany.skin2.home.model.SkinViewEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangfeng on 2017/12/14.
 */

public class SkinStorageMock {
    private static final String AD_TOP_NAME = "ad_top_name";
    private static final String AD_DYNAMIC_NAME = "ad_name";

    private static final String[] indicators = {
            "http://pics.sc.chinaz.com/Files/pic/icons128/5652/13.png",
            "https://pic.pimg.tw/judy725725/1396510971-668284893.gif",
            "http://pics.sc.chinaz.com/Files/pic/icons128/5652/29.png",
    };

    private final static SkinViewEntity topAdStub = new SkinViewEntity();
    private final static SkinViewEntity dynamicAdStub = new SkinViewEntity();

    public final static List<SkinViewEntity> homeViewEntitiesCache = new ArrayList<>();
    private final static SkinCategoryGroup collection = new SkinCategoryGroup();
    private final static SkinCategoryGroup categories = new SkinCategoryGroup();
    private final static SkinCategoryGroup fullCategories = new SkinCategoryGroup();

    private final static SkinCategory feature = new SkinCategory();
    private final static SkinCategory trending = new SkinCategory();
    private final static SkinCategory allThemes = new SkinCategory();


    private final static SkinCategory fullBundles = new SkinCategory();
    public final static List<SkinViewEntity> categoryGroupCache = new ArrayList<>();
    public final static List<SkinViewEntity> categoryViewCache = new ArrayList<>();
    
    private static void addCollectionCategoryGroup(SkinCategoryGroup collection) {
        appendCategoryGroup(collection, "3D", "Selected/3D_1505126102.png");
        appendCategoryGroup(collection, "Multicolor", "Selected/Multicolor_1504084997.png");
        appendCategoryGroup(collection, "Gold", "Selected/Gold_1504084961.png");
        appendCategoryGroup(collection, "Pink", "Selected/Pink_1504085104.png");
        appendCategoryGroup(collection, "Blue", "Selected/Blue_1504085018.png");
        appendCategoryGroup(collection, "Silver", "Selected/Silver_1504085196.png");
    }

    private static void addSimpleCategories(SkinCategoryGroup categories) {
        appendCategoryGroup(categories, "Cool", "ThemeGroup/cool.png");
        appendCategoryGroup(categories, "Sports", "ThemeGroup/sports.png");
        appendCategoryGroup(categories, "Love", "ThemeGroup/LOVE111.png");
        appendCategoryGroup(categories, "Cartoon", "ThemeGroup/cartoon111.png");
    }

    private static void fillFeatureBundle(SkinCategory feature) {
        appendBundleCategory(feature, "Fire Eye Wolf", "Theme/7000000947_1513080567.png");
        appendBundleCategory(feature, "Pocket Monster", "Theme/7000000944_1513079629.png");
        feature.getBundleList().add(topAdStub);
        appendBundleCategory(feature, "Snow Christmas", "Theme/8000000332_1513054401.png");
        appendBundleCategory(feature, "Fire Ninja Boy", "Theme/7000000948_1513131945.png");
        appendBundleCategory(feature, "Reggae Rasta", "Theme/Reggae_Rasta.png");
        appendBundleCategory(feature, "Pink Monster", "Theme/7000000905_1511953721.png");
        appendBundleCategory(feature, "Birthday Blue Cat", "Theme/7000000892_1511951123.webp");
        appendBundleCategory(feature, "Pink Wizard Monster", "Theme/7000000923_1512814549.webp");
    }

    private static void fillTrendingBundle(SkinCategory trending) {
        appendBundleCategory(trending, "Merry Christmas", "Theme/7000000919_1512718775.webp");
        appendBundleCategory(trending, "Blue Monster", "Theme/8000000255_1508142748.png");
    }

    private static void fillAllBundle(SkinCategory allThemes) {
        appendBundleCategory(allThemes, "Merry Christmas", "Theme/7000000919_1512718775.webp");
        appendBundleCategory(allThemes, "Blue Monster", "Theme/8000000255_1508142748.png");
        appendBundleCategory(allThemes, "Fire Eye Wolf", "Theme/7000000947_1513080567.png");
        appendBundleCategory(allThemes, "Pocket Monster", "Theme/7000000944_1513079629.png");
        allThemes.getBundleList().add(dynamicAdStub);
        appendBundleCategory(allThemes, "Snow Christmas", "Theme/8000000332_1513054401.png");
        appendBundleCategory(allThemes, "Fire Ninja Boy", "Theme/7000000948_1513131945.png");
        appendBundleCategory(allThemes, "Reggae Rasta", "Theme/Reggae_Rasta.png");
        appendBundleCategory(allThemes, "Pink Monster", "Theme/7000000905_1511953721.png");
        appendBundleCategory(allThemes, "Birthday Blue Cat", "Theme/7000000892_1511951123.webp");
        appendBundleCategory(allThemes, "Pink Wizard Monster", "Theme/7000000923_1512814549.webp");
    }

    static {
        topAdStub.setBundleName(AD_TOP_NAME);
        dynamicAdStub.setBundleName(AD_DYNAMIC_NAME);

        addCollectionCategoryGroup(collection);
        collection.setBundleName("Collection");
        collection.setDisplayColumn(0);
        collection.setHasMore(true);

        addSimpleCategories(categories);
        categories.setBundleName("Categories");
        categories.setDisplayColumn(2);
        categories.setHasMore(true);

        fillFeatureBundle(feature);
        feature.setBundleName("Feature");
        feature.setDisplayColumn(2);
        feature.setHasMore(true);

        fillTrendingBundle(trending);
        trending.setBundleName("Trending");
        trending.setDisplayColumn(2);
        trending.setHasMore(true);

        fillAllBundle(allThemes);
        allThemes.setBundleName("All Themes");
        allThemes.setDisplayColumn(2);
        allThemes.setHasMore(false);

        homeViewEntitiesCache.add(collection);
        homeViewEntitiesCache.add(categories);
        homeViewEntitiesCache.add(feature);
        homeViewEntitiesCache.add(trending);
        homeViewEntitiesCache.add(allThemes);

        addSimpleCategories(fullCategories);
        addCollectionCategoryGroup(fullCategories);
        categoryGroupCache.add(collection);
        categoryGroupCache.add(fullCategories);

        fillAllBundle(fullBundles);
        fillTrendingBundle(fullBundles);
        fillFeatureBundle(fullBundles);
        categoryViewCache.addAll(fullBundles.getBundleList());
    }

    private static void appendBundleCategory(SkinCategory category, String name, String urlSuffix) {
        SkinBundle bundle = new SkinBundle();
        bundle.setBundleName(name);
        bundle.setPreviewUrl(getUrl(urlSuffix));
        bundle.setFileSize(3484267);
        bundle.setAdShow(name.length()%2 == 0);
        bundle.setApkUrl("https://play.google.com/store/apps/details?id=fire.eye.wolf.cool.thriller.typany.u7000000947&referrer=tplist");
        bundle.setCanShare(urlSuffix.length()%2==0);
        bundle.setFileUrl(getUrl("Theme/7000000947_1512993960.ssf"));
        bundle.setHotFlag((name.length() + urlSuffix.length())%2==0);

        List iconList = bundle.getIconList();
        for (int i = 0; i < name.length()%indicators.length; i++) {
            iconList.add(indicators[i]);
        }

        category.getBundleList().add(bundle);
    }

    private static void appendCategoryGroup(SkinCategoryGroup group, String name, String urlSuffix) {
        SkinViewEntity entity = new SkinViewEntity();
        entity.setBundleName(name);
        entity.setPreviewUrl(getUrl(urlSuffix));
        group.getBundleList().add(entity);
    }
    private static String getUrl(String urlSuffix) {
        return "http://d2ezgnxmilyqe4.cloudfront.net/media/" + urlSuffix;
    }
}
